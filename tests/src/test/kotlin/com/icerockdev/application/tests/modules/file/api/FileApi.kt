package com.icerockdev.application.tests.modules.file.api

import com.icerockdev.application.tests.common.api.client.ApiClient
import org.springframework.boot.test.context.TestComponent
import org.springframework.http.HttpMethod
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.result.StatusResultMatchersDsl

@TestComponent
class FileApi : ApiClient() {

    lateinit var accessToken: String

    inline fun <reified O> uploadFile(
        body: RequestBody<MockMultipartFile>,
        crossinline statusMatcher: StatusResultMatchersDsl.() -> Unit = { isOk() }
    ): ResponseBody<O> {
        return multipartFileRequest(
            method = HttpMethod.POST,
            url = "/v1/file/upload",
            accessToken = accessToken,
            request = body,
            statusMatcher = statusMatcher
        )
    }
}
