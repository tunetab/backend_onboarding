package com.icerockdev.application.tests.application.response

import com.icerockdev.application.tests.application.api.AuthApi
import com.icerockdev.application.tests.common.api.client.ApiClient
import com.icerockdev.application.tests.common.db.annotations.SpringBootTestWithDB
import kotlin.test.assertContains
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import org.openapitools.client.models.ErrorDetail
import org.openapitools.client.models.SignInRequestBody
import org.springframework.beans.factory.annotation.Autowired

@SpringBootTestWithDB
class ErrorResponseTests {
    @Autowired
    lateinit var authApi: AuthApi

    @Test
    fun validationExceptionTest() {
        val errorResponse =
            authApi.signInRequest<SignInRequestBody, com.icerockdev.api.response.ResponseList<ErrorDetail>>(
                body = ApiClient.RequestBody(
                    SignInRequestBody(
                        email = "dadada",
                        password = "12345678"
                    )
                ),
                statusMatcher = { isUnprocessableEntity() }
            )

        assertFalse { errorResponse.success }
        assertTrue { errorResponse.data.items.count() == 1 }
        val errorFieldList = errorResponse.data.items.map { it.field }
        assertContains(errorFieldList, "email")
    }

    @Test
    fun mappingExceptionTest() {
        val errorResponse = authApi.signInRequest<Any, com.icerockdev.api.response.ResponseList<ErrorDetail>>(
            body = ApiClient.RequestBody(
                object {}
            ),
            statusMatcher = { isBadRequest() }
        )

        assertFalse { errorResponse.success }
    }
}
