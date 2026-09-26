package com.icerockdev.application.tests.application.feature.auth

import com.icerockdev.application.auth.jwt.CustomJWTTokenGenerator
import com.icerockdev.application.tests.application.api.AuthApi
import com.icerockdev.application.tests.application.api.UserApi
import com.icerockdev.application.tests.common.api.client.ApiClient
import com.icerockdev.application.tests.common.api.client.response.ResponseList
import com.icerockdev.application.tests.common.db.annotations.SpringBootTestWithDB
import com.icerockdev.application.tests.fixture.AuthFixtureBuilder
import com.icerockdev.application.tests.fixture.UserFixtureBuilder
import com.icerockdev.auth.acl.Role
import com.icerockdev.auth.jwt.JwtTokens
import com.icerockdev.auth.jwt.TokenTypes
import com.icerockdev.tests.util.sleep
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.openapitools.client.models.ChangeRestorePasswordRequestBody
import org.openapitools.client.models.ErrorDetail
import org.openapitools.client.models.RefreshTokenRequestBody
import org.openapitools.client.models.RestorePasswordByPhoneRequestBody
import org.openapitools.client.models.RestorePasswordRequestBody
import org.openapitools.client.models.SignInByPhoneRequestBody
import org.openapitools.client.models.SignInRequestBody
import org.openapitools.client.models.SignUpRequestBody
import org.openapitools.client.models.TokensData
import org.springframework.beans.factory.annotation.Autowired

@SpringBootTestWithDB
class AuthApiTests {

    @Autowired
    private lateinit var authApi: AuthApi

    @Autowired
    private lateinit var userApi: UserApi

    @Autowired
    private lateinit var jwtTokenGenerator: CustomJWTTokenGenerator

    @Autowired
    private lateinit var authFixtureBuilder: AuthFixtureBuilder

    @Autowired
    private lateinit var userFixtureBuilder: UserFixtureBuilder

    @Test
    fun signUpTest() {
        val tokens = authApi.signUpRequest<SignUpRequestBody, TokensData>(
            ApiClient.RequestBody(
                SignUpRequestBody(
                    email = "dada@sasa.ee",
                    phone = "70000000000",
                    password = "12345678"
                )
            )
        )
        assertTrue { jwtTokenGenerator.verifier.isSigned(tokens.data.accessToken) }
        assertDoesNotThrow { jwtTokenGenerator.verifier.parse(tokens.data.accessToken) }
        assertEquals(
            TokenTypes.TOKEN_TYPE_ACCESS.value,
            jwtTokenGenerator.verifier.parseClaimsJws(tokens.data.accessToken).body["type"]
        )
        assertTrue { jwtTokenGenerator.verifier.isSigned(tokens.data.refreshToken) }
        assertEquals(
            TokenTypes.TOKEN_TYPE_REFRESH.value,
            jwtTokenGenerator.verifier.parseClaimsJws(tokens.data.refreshToken).body["type"]
        )
    }

    @Test
    fun signInTest() {
        authApi.signUpRequest<SignUpRequestBody, TokensData>(
            ApiClient.RequestBody(
                SignUpRequestBody(
                    email = "dada@sasa.ee",
                    phone = "70000000000",
                    password = "12345678"
                )
            )
        )
        authApi.signInRequest<SignInRequestBody, TokensData>(
            ApiClient.RequestBody(SignInRequestBody(email = "dada@sasa.ee", password = "12345678"))
        )
    }

    @Test
    fun signInByPhoneTest() {
        authApi.signUpRequest<SignUpRequestBody, TokensData>(
            ApiClient.RequestBody(
                SignUpRequestBody(
                    email = "dada@sasa.ee",
                    phone = "70000000000",
                    password = "12345678"
                )
            )
        )
        authApi.signInByPhoneRequest<SignInByPhoneRequestBody, TokensData>(
            ApiClient.RequestBody(
                SignInByPhoneRequestBody(phone = "70000000000", password = "12345678")
            )
        )
    }

    @Test
    fun logOutTest() {
        val tokens = authApi.signUpRequest<SignUpRequestBody, TokensData>(
            ApiClient.RequestBody(
                SignUpRequestBody(
                    email = "dada@sasa.ee",
                    phone = "70000000000",
                    password = "12345678"
                )
            )
        )
        authApi.logOut<RefreshTokenRequestBody, Any>(
            body = ApiClient.RequestBody(
                RefreshTokenRequestBody(tokens.data.refreshToken)
            ), accessToken = tokens.data.accessToken
        )
        authApi.logOut<RefreshTokenRequestBody, ResponseList<ErrorDetail>>(
            body = ApiClient.RequestBody(
                RefreshTokenRequestBody(tokens.data.refreshToken)
            ),
            accessToken = tokens.data.accessToken
        ) {
            isUnauthorized()
        }
    }

    @Test
    fun revokeTokenTest() {
        val tokens = authApi.signUpRequest<SignUpRequestBody, TokensData>(
            ApiClient.RequestBody(
                SignUpRequestBody(
                    email = "dada@sasa.ee",
                    phone = "70000000000",
                    password = "12345678"
                )
            )
        )
        authApi.logOut<RefreshTokenRequestBody, Any>(
            body = ApiClient.RequestBody(
                RefreshTokenRequestBody(tokens.data.refreshToken)
            ), accessToken = tokens.data.accessToken
        )
        authApi.logOut<RefreshTokenRequestBody, ResponseList<ErrorDetail>>(
            body = ApiClient.RequestBody(
                RefreshTokenRequestBody(tokens.data.refreshToken)
            ),
            accessToken = tokens.data.accessToken
        ) {
            isUnauthorized()
        }

        sleep(1000)

        val newTokens = authApi.signInRequest<SignInRequestBody, TokensData>(
            ApiClient.RequestBody(
                SignInRequestBody(email = "dada@sasa.ee", password = "12345678")
            )
        )
        authApi.logOut<RefreshTokenRequestBody, Any>(
            body = ApiClient.RequestBody(
                RefreshTokenRequestBody(newTokens.data.refreshToken)
            ),
            accessToken = newTokens.data.accessToken
        )
    }

    @Test
    fun refreshTokenTest() {
        val tokens = authApi.signUpRequest<SignUpRequestBody, TokensData>(
            ApiClient.RequestBody(
                SignUpRequestBody(
                    email = "dada@sasa.ee",
                    phone = "70000000000",
                    password = "12345678"
                )
            )
        )

        val newTokens = authApi.refreshTokenRequest<RefreshTokenRequestBody, TokensData>(
            ApiClient.RequestBody(
                RefreshTokenRequestBody(tokens.data.refreshToken)
            )
        )
        authApi.logOut<RefreshTokenRequestBody, Any>(
            body = ApiClient.RequestBody(
                RefreshTokenRequestBody(newTokens.data.refreshToken)
            ),
            accessToken = newTokens.data.accessToken
        )
    }

    @Test
    fun deleteAccountAndRegisterAgainTest() {
        val tokens = authApi.signUpRequest<SignUpRequestBody, TokensData>(
            ApiClient.RequestBody(
                SignUpRequestBody(
                    email = "dada@sasa.ee",
                    phone = "70000000000",
                    password = "12345678"
                )
            )
        )
        userApi.accessToken = tokens.data.accessToken
        userApi.deleteSelfAccountRequest<Any>()
        authApi.signUpRequest<SignUpRequestBody, TokensData>(
            ApiClient.RequestBody(
                SignUpRequestBody(
                    email = "dada@sasa.ee",
                    phone = "70000000000",
                    password = "12345678"
                )
            )
        )
    }

    @Test
    fun restorePasswordByPhoneTest() {
        val user = userFixtureBuilder.buildUser(
            email = "user@sasa.ee",
            phone = "71234567890",
            password = "12345678",
            role = Role.USER
        )

        assertDoesNotThrow {
            authApi.restorePasswordByPhoneRequest<RestorePasswordByPhoneRequestBody, Any>(
                ApiClient.RequestBody(
                    RestorePasswordByPhoneRequestBody(phone = user.phone)
                )
            )
        }

        val confirmationToken = authFixtureBuilder.getConfirmationCode(user)
        assertNotNull(actual = confirmationToken)

        val newPassword = "987654321"
        assertDoesNotThrow {
            authApi.restorePasswordChangeByPhoneRequest<ChangeRestorePasswordRequestBody, Any>(
                ApiClient.RequestBody(
                    ChangeRestorePasswordRequestBody(
                        confirmationToken = confirmationToken.token,
                        password = newPassword,
                        passwordRepeat = newPassword,
                        phone = user.phone
                    )
                )
            )
        }

        authApi.signInRequest<SignInRequestBody, TokensData>(
            ApiClient.RequestBody(
                SignInRequestBody(
                    email = user.email,
                    password = newPassword
                )
            )
        )
    }

    @Test
    fun restorePasswordRateLimitByPhoneTest() {
        val user = userFixtureBuilder.buildUser(
            email = "user@sasa.ee",
            phone = "71234567890",
            password = "12345678",
            role = Role.USER
        )

        assertDoesNotThrow {
            authApi.restorePasswordByPhoneRequest<RestorePasswordByPhoneRequestBody, Any>(
                ApiClient.RequestBody(RestorePasswordByPhoneRequestBody(phone = user.phone))
            )
        }

        authApi.restorePasswordByPhoneRequest<RestorePasswordByPhoneRequestBody, ResponseList<ErrorDetail>>(
            body = ApiClient.RequestBody(RestorePasswordByPhoneRequestBody(phone = user.phone)),
            statusMatcher = { isBadRequest() }
        )
    }

    @Test
    fun restorePasswordByEmailTest() {
        val user = userFixtureBuilder.buildUser(
            email = "user@sasa.ee",
            phone = "71234567890",
            password = "12345678",
            role = Role.USER
        )

        assertDoesNotThrow {
            authApi.restorePasswordByEmailRequest<RestorePasswordRequestBody, Any?>(
                ApiClient.RequestBody(
                    RestorePasswordRequestBody(email = user.email)
                )
            )
        }

        val confirmationToken = authFixtureBuilder.getConfirmationToken(user)
        assertNotNull(actual = confirmationToken)

        val newPassword = "987654321"
        assertDoesNotThrow {
            authApi.restorePasswordChangeByEmailRequest<ChangeRestorePasswordRequestBody, Any?>(
                ApiClient.RequestBody(
                    ChangeRestorePasswordRequestBody(
                        confirmationToken = confirmationToken.token,
                        password = newPassword,
                        passwordRepeat = newPassword
                    )
                )
            )
        }

        authApi.signInRequest<SignInRequestBody, JwtTokens>(
            ApiClient.RequestBody(
                SignInRequestBody(
                    email = user.email,
                    password = "987654321"
                )
            )
        )
    }

    @Test
    fun restorePasswordRateLimitByEmailTest() {
        val user = userFixtureBuilder.buildUser(
            email = "user@sasa.ee",
            phone = "71234567890",
            password = "12345678",
            role = Role.USER
        )

        assertDoesNotThrow {
            authApi.restorePasswordByEmailRequest<RestorePasswordRequestBody, Any?>(
                ApiClient.RequestBody(
                    RestorePasswordRequestBody(email = user.email)
                )
            )
        }

        authApi.restorePasswordByEmailRequest<RestorePasswordRequestBody, Any?>(
            ApiClient.RequestBody(
                RestorePasswordRequestBody(email = user.email)
            )
        ) {
            isBadRequest()
        }
    }
}
