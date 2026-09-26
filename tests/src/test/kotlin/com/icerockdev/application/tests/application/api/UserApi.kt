package com.icerockdev.application.tests.application.api

import com.icerockdev.application.tests.common.api.client.ApiClient
import org.springframework.boot.test.context.TestComponent
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.result.StatusResultMatchersDsl

@TestComponent
class UserApi : ApiClient() {
    lateinit var accessToken: String

    inline fun <reified O> getProfileRequest(
        crossinline statusMatcher: StatusResultMatchersDsl.() -> Unit = { isOk() }
    ): ResponseBody<O> {
        return request(
            method = HttpMethod.GET,
            url = "/v1/user/profile",
            request = null,
            accessToken = accessToken,
            statusMatcher = statusMatcher
        )
    }

    inline fun <reified I, reified O> updateProfileRequest(
        body: RequestBody<I>,
        crossinline statusMatcher: StatusResultMatchersDsl.() -> Unit = { isOk() }
    ): ResponseBody<O> {
        return request(
            method = HttpMethod.PUT,
            url = "/v1/user/profile",
            request = body,
            statusMatcher = statusMatcher,
            accessToken = accessToken
        )
    }

    inline fun <reified O> deleteSelfAccountRequest(): ResponseBody<O> {
        return request(
            method = HttpMethod.DELETE,
            url = "/v1/user",
            request = null,
            accessToken = accessToken
        )
    }
}
