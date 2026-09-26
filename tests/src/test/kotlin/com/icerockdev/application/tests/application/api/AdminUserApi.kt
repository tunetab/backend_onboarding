package com.icerockdev.application.tests.application.api

import com.icerockdev.application.tests.common.api.client.ApiClient
import com.icerockdev.application.tests.common.api.client.pagination.Pagination
import java.util.UUID
import org.springframework.boot.test.context.TestComponent
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.result.StatusResultMatchersDsl

@TestComponent
class AdminUserApi : ApiClient() {
    lateinit var accessToken: String

    inline fun <reified I, reified O> createUserRequest(
        body: RequestBody<I>,
    ): ResponseBody<O> {
        return request<RequestBody<I>, ResponseBody<O>>(
            method = HttpMethod.POST,
            url = "/v1/admin/user",
            request = body,
            accessToken = accessToken
        )
    }

    inline fun <reified I, reified O> updateUserRequest(
        userId: UUID,
        body: RequestBody<I>,
        crossinline statusMatcher: StatusResultMatchersDsl.() -> Unit = { isOk() }
    ): ResponseBody<O> {
        return request<RequestBody<I>, ResponseBody<O>>(
            method = HttpMethod.PUT,
            url = "/v1/admin/user/$userId",
            request = body,
            statusMatcher = statusMatcher,
            accessToken = accessToken
        )
    }

    inline fun <reified O> deleteUserRequest(
        userId: UUID,
        crossinline statusMatcher: StatusResultMatchersDsl.() -> Unit = { isOk() }
    ): ResponseBody<O> {
        return request<RequestBody<*>, ResponseBody<O>>(
            method = HttpMethod.DELETE,
            url = "/v1/admin/user/$userId",
            request = null,
            statusMatcher = statusMatcher,
            accessToken = accessToken
        )
    }

    inline fun <reified O> getUserRequest(
        userId: UUID
    ): ResponseBody<O> {
        return request<RequestBody<*>, ResponseBody<O>>(
            method = HttpMethod.GET,
            url = "/v1/admin/user/$userId",
            request = null,
            accessToken = accessToken
        )
    }

    inline fun <reified O> getUserListRequest(
        withPagination: () -> Pagination? = { null },
        withFilter: () -> Map<String, List<String>> = { emptyMap() }
    ): ResponseBody<O> {
        return request<RequestBody<*>, ResponseBody<O>>(
            method = HttpMethod.GET,
            url = "/v1/admin/user",
            parameters = withPagination()?.toMap()?.let {
                it.putAll(withFilter())
                it
            } ?: withFilter(),
            request = null,
            accessToken = accessToken
        )
    }

    inline fun <reified O> blockUserRequest(
        userId: UUID
    ): ResponseBody<O> {
        return request<RequestBody<*>, ResponseBody<O>>(
            method = HttpMethod.PUT,
            url = "/v1/admin/user/$userId/block",
            request = null,
            accessToken = accessToken
        )
    }

    inline fun <reified O> unblockUserRequest(
        userId: UUID
    ): ResponseBody<O> {
        return request<RequestBody<*>, ResponseBody<O>>(
            method = HttpMethod.PUT,
            url = "/v1/admin/user/$userId/unblock",
            request = null,
            accessToken = accessToken
        )
    }
}
