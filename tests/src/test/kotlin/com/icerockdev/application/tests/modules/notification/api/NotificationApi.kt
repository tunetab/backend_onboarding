package com.icerockdev.application.tests.modules.notification.api


import com.icerockdev.application.tests.common.api.client.ApiClient
import com.icerockdev.application.tests.common.api.client.pagination.Pagination
import org.springframework.boot.test.context.TestComponent
import org.springframework.http.HttpMethod

@TestComponent
class NotificationApi : ApiClient() {
    lateinit var accessToken: String

    inline fun <reified I, reified O> addPushTokenRequest(
        body: RequestBody<I>
    ): ResponseBody<O> {
        return request<RequestBody<I>, ResponseBody<O>>(
            method = HttpMethod.POST,
            url = "/v1/notification/token",
            accessToken = accessToken,
            request = body
        )
    }

    inline fun <reified O> deletePushTokenRequest(
        pushToken: String
    ): ResponseBody<O> {
        return request<RequestBody<*>, ResponseBody<O>>(
            method = HttpMethod.DELETE,
            url = "/v1/notification/token/${pushToken}",
            accessToken = accessToken,
            request = null
        )
    }

    inline fun <reified O> getNotificationListRequest(
        withPagination: () -> Pagination? = { null }
    ): ResponseBody<O> {
        return request<RequestBody<*>, ResponseBody<O>>(
            method = HttpMethod.GET,
            url = "/v1/notification",
            accessToken = accessToken,
            parameters = withPagination()?.toMap(),
            request = null
        )
    }

    inline fun <reified O> setNotificationAsReadRequest(
        id: String
    ): ResponseBody<O> {
        return request<RequestBody<*>, ResponseBody<O>>(
            method = HttpMethod.PUT,
            url = "/v1/notification/$id/read",
            accessToken = accessToken,
            request = null
        )
    }

    inline fun <reified O> setAllNotificationAsReadRequest(): ResponseBody<O> {
        return request<RequestBody<*>, ResponseBody<O>>(
            method = HttpMethod.PUT,
            url = "/v1/notification/read",
            accessToken = accessToken,
            request = null
        )
    }

    inline fun <reified O> deleteNotificationRequest(
        id: String
    ): ResponseBody<O> {
        return request<RequestBody<*>, ResponseBody<O>>(
            method = HttpMethod.DELETE,
            url = "/v1/notification/$id",
            accessToken = accessToken,
            request = null
        )
    }

    inline fun <reified O> getNotificationSettingsRequest(): ResponseBody<O> {
        return request<RequestBody<*>, ResponseBody<O>>(
            method = HttpMethod.GET,
            url = "/v1/notification/settings",
            accessToken = accessToken,
            request = null
        )
    }

    inline fun <reified I, reified O> updateNotificationSettingsRequest(
        body: RequestBody<I>
    ): ResponseBody<O> {
        return request<RequestBody<I>, ResponseBody<O>>(
            method = HttpMethod.PUT,
            url = "/v1/notification/settings",
            accessToken = accessToken,
            request = body
        )
    }
}
