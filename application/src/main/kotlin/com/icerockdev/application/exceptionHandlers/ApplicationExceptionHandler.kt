package com.icerockdev.application.exceptionHandlers

import com.fasterxml.jackson.databind.JsonMappingException
import com.icerockdev.api.response.ErrorListResponse
import com.icerockdev.api.response.Response
import com.icerockdev.exception.ErrorDetail
import com.icerockdev.exception.ExtUserException
import com.icerockdev.exception.UserException
import com.icerockdev.i18n.I18N
import com.icerockdev.i18n.LK
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import jakarta.validation.ConstraintViolationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.data.mapping.PropertyReferenceException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.AuthenticationException
import org.springframework.validation.BindException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.servlet.NoHandlerFoundException


@Order(value = Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
class ApplicationExceptionHandler(
    private val i18N: I18N
) {

    @ExceptionHandler(
        value = [
            AuthenticationException::class,
            JwtException::class,
            ExpiredJwtException::class
        ]
    )
    fun handleAuthException(exception: Exception): ResponseEntity<Response<*>> {
        logger.warn(exception.localizedMessage, exception)

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ErrorListResponse(
                listOf(
                    ErrorDetail(exception.localizedMessage ?: HttpStatus.UNAUTHORIZED.reasonPhrase)
                )
            )
        )
    }

    @ExceptionHandler(
        value = [
            InsufficientAuthenticationException::class,
        ]
    )
    fun handleInsufficientAuthenticationException(exception: Exception): ResponseEntity<Response<*>> {
        logger.warn(exception.localizedMessage, exception)
        // обработка только для кривых jwt токенов
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ErrorListResponse(
                listOf(
                    ErrorDetail(i18N.t(LK.authFailedRefreshToken))
                )
            )
        )
    }

    @ExceptionHandler(value = [HttpRequestMethodNotSupportedException::class])
    fun handleHttpRequestMethodNotSupportedExceptions(
        exception: HttpRequestMethodNotSupportedException
    ): ResponseEntity<Response<*>> {
        logger.warn(exception.localizedMessage, exception)

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED.value()).body(
            ErrorListResponse(
                listOf(
                    ErrorDetail(HttpStatus.METHOD_NOT_ALLOWED.reasonPhrase)
                )
            )
        )
    }

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun handleHttpMessageNotReadableExceptions(
        exception: HttpMessageNotReadableException
    ): ResponseEntity<Response<*>> {
        logger.warn(exception.localizedMessage, exception)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
            ErrorListResponse(
                listOf(
                    ErrorDetail(HttpStatus.BAD_REQUEST.reasonPhrase)
                )
            )
        )
    }

    @ExceptionHandler(
        value = [
            JsonMappingException::class,
            MethodArgumentTypeMismatchException::class,
            MethodArgumentNotValidException::class,
            ConstraintViolationException::class,
            PropertyReferenceException::class,
            BindException::class
        ]
    )
    fun handleValidationExceptions(exception: Throwable): ResponseEntity<Response<*>> {
        logger.warn(exception.localizedMessage, exception)

        val errorListResponse = when (exception) {
            is JsonMappingException -> {
                ErrorListResponse(
                    listOf(ErrorDetail(message = "Json mapping error", field = exception.path.lastOrNull()?.fieldName))
                )
            }

            is MethodArgumentTypeMismatchException -> {
                ErrorListResponse(
                    listOf(ErrorDetail(message = "Json mapping error", field = exception.parameter.parameterName))
                )
            }

            is MethodArgumentNotValidException -> {
                ErrorListResponse(
                    exception.bindingResult.fieldErrors.map {
                        ErrorDetail(
                            message = it.defaultMessage ?: "",
                            field = it.field
                        )
                    }
                )
            }

            is ConstraintViolationException -> {
                ErrorListResponse(exception.constraintViolations ?: emptySet())
            }

            is PropertyReferenceException -> {
                ErrorListResponse(
                    listOf(ErrorDetail(message = exception.localizedMessage, field = exception.propertyName))
                )
            }

            is BindException -> {
                ErrorListResponse(exception.bindingResult.fieldErrors.map {
                    ErrorDetail(message = it.defaultMessage ?: "", field = it.field)
                })
            }

            else -> ErrorListResponse()
        }
        return ResponseEntity.unprocessableEntity().body(
            errorListResponse
        )
    }

    @ExceptionHandler(value = [UserException::class])
    fun handleUserException(exception: UserException): ResponseEntity<Response<*>> {
        logger.warn(exception.localizedMessage, exception)
        return ResponseEntity.status(exception.status).body(exception.getErrorResponse())
    }

    @ExceptionHandler(value = [ExtUserException::class])
    fun handleExtUserException(exception: ExtUserException): ResponseEntity<Response<*>> {
        logger.warn(exception.localizedMessage, exception)
        return ResponseEntity.status(exception.status).body(exception.getErrorResponse())
    }

    @ExceptionHandler(IllegalStateException::class)
    fun onIllegalStateException(exception: IllegalStateException): ResponseEntity<Response<*>> {
        val errorListResponse = when (val cause = exception.cause) {
            is UserException -> {
                logger.warn(exception.localizedMessage, exception)
                cause.getErrorResponse()
            }

            else -> {
                logger.error(exception.localizedMessage, exception)
                ErrorListResponse(
                    listOf(
                        ErrorDetail(message = HttpStatus.BAD_REQUEST.reasonPhrase)
                    )
                )
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorListResponse)
    }

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun onFileSizeLimitException(exception: MaxUploadSizeExceededException): ResponseEntity<Response<*>> {
        logger.warn(exception.localizedMessage, exception)
        val errorListResponse = ErrorListResponse(
            listOf(
                ErrorDetail(message = HttpStatus.PAYLOAD_TOO_LARGE.reasonPhrase)
            )
        )

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(errorListResponse)
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun onNoHandlerFoundException(exception: NoHandlerFoundException): ResponseEntity<Response<*>> {
        logger.warn(exception.localizedMessage, exception)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorListResponse(
                listOf(
                    ErrorDetail(message = HttpStatus.NOT_FOUND.reasonPhrase)
                )
            )
        )
    }

    @ExceptionHandler(Throwable::class)
    fun onThrowable(exception: Throwable): ResponseEntity<Response<*>> {
        logger.error(exception.localizedMessage, exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorListResponse(
                listOf(
                    ErrorDetail(message = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase)
                )
            )
        )
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(ApplicationExceptionHandler::class.java)
    }
}
