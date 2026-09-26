package com.icerockdev.application.tests.application.feature.user

import com.icerockdev.application.tests.application.api.AuthApi
import com.icerockdev.application.tests.application.api.UserApi
import com.icerockdev.application.tests.common.api.client.ApiClient
import com.icerockdev.application.tests.common.api.client.response.ResponseList
import com.icerockdev.application.tests.common.db.annotations.SpringBootTestWithDB
import com.icerockdev.application.tests.fixture.AuthFixtureBuilder
import com.icerockdev.application.tests.fixture.UserFixtureBuilder
import com.icerockdev.application.user.model.User
import com.icerockdev.auth.acl.Role
import com.icerockdev.auth.jwt.JwtTokens
import com.icerockdev.exception.NotFoundException
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.openapitools.client.models.ErrorDetail
import org.openapitools.client.models.RefreshTokenRequestBody
import org.openapitools.client.models.UpdateUserProfileRequestBody
import org.openapitools.client.models.UserProfileInfo
import org.springframework.beans.factory.annotation.Autowired

@SpringBootTestWithDB
class UserTests {

    @Autowired
    private lateinit var userApi: UserApi

    @Autowired
    private lateinit var authApi: AuthApi

    @Autowired
    private lateinit var authFixtureBuilder: AuthFixtureBuilder

    @Autowired
    private lateinit var userFixtureBuilder: UserFixtureBuilder

    private lateinit var user: User

    private lateinit var jwtTokens: JwtTokens

    @BeforeEach
    fun initUser() {
        user = userFixtureBuilder.buildUser(
            email = "user@sasa.ee",
            phone = "71234567890",
            password = "12345678",
            role = Role.USER
        )
        jwtTokens = authFixtureBuilder.authorizeUser(user.email, "12345678")
        userApi.accessToken = jwtTokens.accessToken
    }

    @Test
    fun testGetEmptyProfile() {
        val response = userApi.getProfileRequest<UserProfileInfo>()
        assertNull(response.data)
    }

    @Test
    fun testUpdateProfile() {
        val body = UpdateUserProfileRequestBody(
            "Firstname",
            "Lastname",
            "Middle-name"
        )

        assertDoesNotThrow {
            userApi.updateProfileRequest<UpdateUserProfileRequestBody, Any>(
                ApiClient.RequestBody(
                    body
                )
            )
        }

        val response = userApi.getProfileRequest<UserProfileInfo>()
        assertEquals(expected = body.firstName, actual = response.data.firstName)
        assertEquals(expected = body.lastName, actual = response.data.lastName)
        assertEquals(expected = body.middleName, actual = response.data.middleName)
    }

    @Test
    fun testDeleteSelfAccount() {
        userApi.deleteSelfAccountRequest<Any?>()

        assertThrows<NotFoundException> {
            authFixtureBuilder.authorizeUser(
                user.email,
                user.password
            )
        }

        userApi.getProfileRequest<ResponseList<ErrorDetail>>(
            statusMatcher = { isUnauthorized() }
        )

        authApi.refreshTokenRequest<RefreshTokenRequestBody, ResponseList<ErrorDetail>>(
            body = ApiClient.RequestBody(
                RefreshTokenRequestBody(jwtTokens.refreshToken)
            ),
            statusMatcher = { isBadRequest() }
        )
    }
}
