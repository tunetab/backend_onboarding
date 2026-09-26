package com.icerockdev.log

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.icerockdev.auth.user.CustomUserDetails
import com.icerockdev.serialization.applyDefaultConfiguration
import com.icerockdev.serialization.applyPrettyPrintConfiguration
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.UUID
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

open class RequestAndResponseLoggingFilter(
    configure: LoggingFilterConfiguration.() -> Unit = { }
) : OncePerRequestFilter() {
    init {
        configuration = defaultLoggingFilterConfiguration().apply(configure)
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response)
        } else {
            doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain)
        }
    }

    private fun doFilterWrapped(
        request: ContentCachingRequestWrapper,
        response: ContentCachingResponseWrapper,
        filterChain: FilterChain
    ) {
        val userUUID = request.userPrincipal?.let {
            (it as UsernamePasswordAuthenticationToken).principal as CustomUserDetails
        }?.id
        val data = mutableMapOf(
            LOG_FIELD_ENV to System.getProperty("env", Environment.LOCAL.value),
            LOG_FIELD_TRACE_UUID to UUID.randomUUID().toString(),
        )

        userUUID?.also {
            data[LOG_FIELD_USER_UUID] = userUUID.toString()
        }

        try {
            beforeRequest(request, response, data)
            filterChain.doFilter(request, response)
        } finally {
            afterRequest(request, response, data)
            data.forEach { (key, value) ->
                MDC.put(key, value)
            }
            log.info(defaultFormat(request, response))
            MDC.clear()
            response.copyBodyToResponse()
        }
    }

    private fun beforeRequest(
        request: ContentCachingRequestWrapper,
        response: ContentCachingResponseWrapper?,
        data: MutableMap<String, String>
    ) {
        logRequestHeader(request, data)
    }

    private fun afterRequest(
        request: ContentCachingRequestWrapper,
        response: ContentCachingResponseWrapper,
        data: MutableMap<String, String>
    ) {
        logRequestBody(request, data)
        logResponse(response, data)
    }

    companion object {
        private const val LOG_FIELD_HEADERS: String = "headers"
        private const val LOG_FIELD_QUERY_PARAMETERS: String = "queryParameters"
        private const val LOG_FIELD_HTTP_METHOD: String = "httpMethod"
        private const val LOG_FIELD_REQUEST_PATH: String = "requestPath"
        private const val LOG_FIELD_REQUEST_BODY: String = "requestBody"
        private const val LOG_FIELD_RESPONSE_BODY: String = "responseBody"
        const val LOG_FIELD_TRACE_UUID: String = "traceUUID"
        private const val LOG_FIELD_STATUS_CODE: String = "statusCode"
        const val LOG_FIELD_ENV: String = "env"
        // use if userId is an integer
        const val LOG_FIELD_USER_ID: String = "userId"
        // use if userId is UUID
        const val LOG_FIELD_USER_UUID: String = "userUUID"

        private val log: Logger = LoggerFactory.getLogger(RequestAndResponseLoggingFilter::class.java)
        private val objectMapper: ObjectMapper = jacksonObjectMapper().apply {
            applyDefaultConfiguration()
            applyPrettyPrintConfiguration()
        }

        private lateinit var configuration: LoggingFilterConfiguration
        private fun defaultLoggingFilterConfiguration(): LoggingFilterConfiguration {
            return LoggingFilterConfiguration(
                visibleMediaTypes = listOf(
                    MediaType.valueOf("text/*"),
                    MediaType.APPLICATION_FORM_URLENCODED,
                    MediaType.APPLICATION_JSON,
                    MediaType.APPLICATION_XML,
                    MediaType.valueOf("application/*+json"),
                    MediaType.valueOf("application/*+xml"),
                    MediaType.MULTIPART_FORM_DATA
                ),
                sensitiveHeaders = listOf(
                    "authorization",
                    "proxy-authorization"
                ),
                sensitiveFields = listOf(
                    "password",
                    "token"
                )
            )
        }

        private fun logRequestHeader(request: ContentCachingRequestWrapper, data: MutableMap<String, String>) {
            data[LOG_FIELD_HTTP_METHOD] = request.method
            data[LOG_FIELD_HEADERS] = getRequestHeadersAsJson(request)
            data[LOG_FIELD_QUERY_PARAMETERS] = getRequestQueryParamsAsJson(request)
            data[LOG_FIELD_REQUEST_PATH] = request.requestURI
        }

        private fun logRequestBody(request: ContentCachingRequestWrapper, data: MutableMap<String, String>) {
            val content = request.contentAsByteArray
            if (content.isNotEmpty()) {
                data[LOG_FIELD_REQUEST_BODY] = getContent(content, request.contentType, request.characterEncoding)
            }
        }

        private fun logResponse(response: ContentCachingResponseWrapper, data: MutableMap<String, String>) {
            data[LOG_FIELD_STATUS_CODE] = response.status.toString()
            val content = response.contentAsByteArray
            if (content.isNotEmpty()) {
                data[LOG_FIELD_RESPONSE_BODY] = getContent(content, response.contentType, response.characterEncoding)
            }
        }

        private fun getContent(
            content: ByteArray,
            contentType: String,
            contentEncoding: String
        ): String {
            val mediaType: MediaType = MediaType.valueOf(contentType)
            val visible = configuration.visibleMediaTypes.any { visibleType: MediaType ->
                visibleType.includes(mediaType)
            }
            if (visible) {
                try {
                    return LoggingHelper.replaceSecretFieldsValueInJsonString(
                        String(content, Charset.forName(contentEncoding)),
                        configuration.sensitiveFields
                    )
                } catch (e: UnsupportedEncodingException) {
                    log.error(e.localizedMessage, e)
                }
            }

            return String.format("[%d bytes content]", content.size)
        }

        private fun getRequestHeadersAsJson(request: ContentCachingRequestWrapper): String {
            val headers = request.headerNames.toList().associateWith { headerName ->
                request.getHeaders(headerName).toList().map {
                    if (isSensitiveHeader(headerName)) {
                        "*******"
                    } else {
                        it
                    }
                }
            }

            return objectMapper.writeValueAsString(headers)
        }

        private fun getResponseHeadersAsJson(response: ContentCachingResponseWrapper): String {
            val headers = response.headerNames.associateWith { headerName ->
                response.getHeaders(headerName).toList().map {
                    if (isSensitiveHeader(headerName)) {
                        "*******"
                    } else {
                        it
                    }
                }
            }

            return objectMapper.writeValueAsString(headers)
        }

        private fun getRequestQueryParamsAsJson(request: ContentCachingRequestWrapper): String {
            return LoggingHelper.replaceSecretFieldsValueInJsonString(
                objectMapper.writeValueAsString(request.parameterMap),
                configuration.sensitiveFields
            )
        }

        /**
         * Determine if a given header name should have its value logged.
         * @param headerName HTTP header name.
         * @return True if the header is sensitive (i.e. its value should **not** be logged).
         */
        private fun isSensitiveHeader(headerName: String): Boolean {
            return configuration.sensitiveHeaders.contains(headerName.lowercase())
        }

        private fun wrapRequest(request: HttpServletRequest): ContentCachingRequestWrapper {
            return if (request is ContentCachingRequestWrapper) {
                request
            } else {
                ContentCachingRequestWrapper(request)
            }
        }

        private fun wrapResponse(response: HttpServletResponse): ContentCachingResponseWrapper {
            return if (response is ContentCachingResponseWrapper) {
                response
            } else {
                ContentCachingResponseWrapper(response)
            }
        }

        private fun defaultFormat(request: HttpServletRequest, response: HttpServletResponse): String {
            return when (val status = HttpStatus.valueOf(response.status)) {
                HttpStatus.FOUND -> "$status: ${request.method} - ${request.requestURI} -> ${
                    response.getHeader(
                        HttpHeaders.LOCATION
                    )
                }"

                else -> "${status.value()} ${status.reasonPhrase}: ${request.method} - ${request.requestURI}"
            }
        }

        enum class Environment(val value: String) {
            DEV("dev"),
            PROD("prod"),
            LOCAL("local")
        }
    }
}
