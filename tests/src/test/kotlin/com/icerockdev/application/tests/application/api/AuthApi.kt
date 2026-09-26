package com.icerockdev.application.tests.application.api

import com.icerockdev.application.tests.common.api.client.ApiClient
import org.springframework.boot.test.context.TestComponent
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.result.StatusResultMatchersDsl

@TestComponent
class AuthApi : ApiClient() {

    inline fun <reified I, reified O> signInRequest(
        body: RequestBody<I>,
        crossinline statusMatcher: StatusResultMatchersDsl.() -> Unit = { isOk() }
    ): ResponseBody<O> {
        return request<RequestBody<I>, ResponseBody<O>>(
            method = HttpMethod.POST,
            url = "/v1/auth/sign-in",
            request = body,
            statusMatcher = statusMatcher,
        )
    }

    inline fun <reified I, reified O> signInByPhoneRequest(
        body: RequestBody<I>,
        crossinline statusMatcher: StatusResultMatchersDsl.() -> Unit = { isOk() }
    ): ResponseBody<O> {
        return request(
            method = HttpMethod.POST,
            url = "/v1/auth/sign-in-by-phone",
            request = body,
            statusMatcher = statusMatcher,
        )
    }

    inline fun <reified I, reified O> signUpRequest(
        body: RequestBody<I>,
        crossinline statusMatcher: StatusResultMatchersDsl.() -> Unit = { isOk() }
    ): ResponseBody<O> {
        return request(
            method = HttpMethod.POST,
            url = "/v1/auth/sign-up",
            request = body,
            langCode = "ru",
            statusMatcher = statusMatcher,
        )
    }

    inline fun <reified I, reified O> refreshTokenRequest(
        body: RequestBody<I>,
        crossinline statusMatcher: StatusResultMatchersDsl.() -> Unit = { isOk() }
    ): ResponseBody<O> {
        return request<RequestBody<I>, ResponseBody<O>>(
            method = HttpMethod.POST,
            url = "/v1/auth/refresh",
            request = body,
            statusMatcher = statusMatcher,
        )
    }

    inline fun <reified I, reified O> logOut(
        body: RequestBody<I>,
        accessToken: String,
        crossinline statusMatcher: StatusResultMatchersDsl.() -> Unit = { isOk() }
    ): ResponseBody<O> {
        return request<RequestBody<I>, ResponseBody<O>>(
            method = HttpMethod.POST,
            url = "/v1/auth/logout",
            accessToken = accessToken,
            request = body,
            statusMatcher = statusMatcher
        )
    }

    inline fun <reified I, reified O> restorePasswordByPhoneRequest(
        body: RequestBody<I>,
        crossinline statusMatcher: StatusResultMatchersDsl.() -> Unit = { isOk() }
    ): ResponseBody<O> {
        return request(
            method = HttpMethod.POST,
            url = "/v1/auth/restore-by-phone",
            request = body,
            statusMatcher = statusMatcher
        )
    }

    inline fun <reified I, reified O> restorePasswordChangeByPhoneRequest(
        body: RequestBody<I>,
        crossinline statusMatcher: StatusResultMatchersDsl.() -> Unit = { isOk() }
    ): ResponseBody<O> {
        return request(
            method = HttpMethod.PUT,
            url = "/v1/auth/restore-by-phone",
            request = body,
            statusMatcher = statusMatcher
        )
    }


    inline fun <reified I, reified O> restorePasswordByEmailRequest(
        body: RequestBody<I>,
        crossinline statusMatcher: StatusResultMatchersDsl.() -> Unit = { isOk() }
    ): ResponseBody<O> {
        return request<RequestBody<I>, ResponseBody<O>>(
            method = HttpMethod.POST,
            url = "/v1/auth/restore-by-email",
            request = body,
            statusMatcher = statusMatcher
        )
    }

    inline fun <reified I, reified O> restorePasswordChangeByEmailRequest(
        body: RequestBody<I>,
        crossinline statusMatcher: StatusResultMatchersDsl.() -> Unit = { isOk() }
    ): ResponseBody<O> {
        return request<RequestBody<I>, ResponseBody<O>>(
            method = HttpMethod.PUT,
            url = "/v1/auth/restore-by-email",
            request = body,
            statusMatcher = statusMatcher
        )
    }
}
