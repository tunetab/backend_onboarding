package com.icerockdev.application.tests.common.api.client

import com.icerockdev.serialization.Serializer
import java.util.Locale
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.multipart
import org.springframework.test.web.servlet.request
import org.springframework.test.web.servlet.result.StatusResultMatchersDsl

open class ApiClient {
    @Autowired
    lateinit var mockMvc: MockMvc

    data class RequestBody<T>(
        val body: T
    )

    data class ResponseBody<T>(
        val timestamp: Long,
        val success: Boolean,
        val data: T
    )

    inline fun <reified I : RequestBody<*>, reified O : ResponseBody<*>> request(
        method: HttpMethod,
        url: String,
        accessToken: String? = null,
        request: I? = null,
        headers: Map<String, String>? = null,
        parameters: Map<String, List<String>>? = null,
        langCode: String? = null,
        crossinline statusMatcher: StatusResultMatchersDsl.() -> Unit = { isOk() }
    ): O {
        val response = mockMvc.request(method, url) {
            accessToken?.also {
                header(HttpHeaders.AUTHORIZATION, "Bearer $it")
            }
            langCode?.also {
                locale(Locale.forLanguageTag(it))
            }
            headers?.forEach { header(it.key, it.value) }
            request?.let {
                content = Serializer.serialize(it.body)
                contentType = MediaType.APPLICATION_JSON
            }
            parameters?.forEach {
                it.value.forEach { value -> param(it.key, value) }
            }

        }.andExpect { status { statusMatcher() } }.andReturn().response.contentAsString

        return Serializer.deserialize(response)
    }

    inline fun <reified O : ResponseBody<*>> multipartFileRequest(
        method: HttpMethod,
        url: String,
        accessToken: String? = null,
        request: RequestBody<MockMultipartFile>? = null,
        headers: Map<String, String>? = null,
        parameters: Map<String, List<String>>? = null,
        langCode: String? = null,
        crossinline statusMatcher: StatusResultMatchersDsl.() -> Unit = { isOk() }
    ): O {
        val response = mockMvc.multipart(method, url) {
            request?.let {
                file(it.body)
            }
            accessToken?.also {
                header(HttpHeaders.AUTHORIZATION, "Bearer $it")
            }
            langCode?.also {
                locale(Locale.forLanguageTag(it))
            }
            headers?.forEach { header(it.key, it.value) }
            parameters?.forEach {
                it.value.forEach { value -> param(it.key, value) }
            }

        }.andExpect { status { statusMatcher() } }.andReturn().response.contentAsString

        return Serializer.deserialize(response)
    }

    fun requestBytes(
        method: HttpMethod,
        url: String,
        accessToken: String? = null,
        headers: Map<String, String>? = null,
        parameters: Map<String, List<String>>? = null,
        langCode: String? = null,
        statusMatcher: StatusResultMatchersDsl.() -> Unit = { isOk() }
    ): MockHttpServletResponse {

        val mvcResult = mockMvc.request(method, url) {
            accessToken?.also {
                header(HttpHeaders.AUTHORIZATION, "Bearer $it")
            }
            langCode?.also {
                locale(Locale.forLanguageTag(it))
            }
            headers?.forEach { header(it.key, it.value) }
            parameters?.forEach {
                it.value.forEach { value -> param(it.key, value) }
            }
        }
            .andExpect { status { statusMatcher() } }
            .andReturn()

        return mvcResult.response
    }
}
