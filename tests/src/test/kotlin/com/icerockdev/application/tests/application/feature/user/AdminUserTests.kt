package com.icerockdev.application.tests.application.feature.user

import com.icerockdev.application.tests.common.api.client.pagination.Pagination
import com.icerockdev.application.tests.common.db.annotations.SpringBootTestWithDB
import com.icerockdev.application.tests.application.api.AdminUserApi
import com.icerockdev.application.tests.common.api.client.ApiClient
import com.icerockdev.application.tests.common.api.client.response.ResponseList
import com.icerockdev.application.tests.fixture.AuthFixtureBuilder
import com.icerockdev.application.tests.fixture.UserFixtureBuilder
import com.icerockdev.auth.acl.Role
import com.icerockdev.exception.NotFoundException
import com.icerockdev.util.HashUtils
import java.util.UUID
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.openapitools.client.models.CreateUserRequestBody
import org.openapitools.client.models.ErrorDetail
import org.openapitools.client.models.RoleProperty
import org.openapitools.client.models.StatusProperty
import org.openapitools.client.models.UpdateUserRequestBody
import org.openapitools.client.models.UserInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.LockedException

@SpringBootTestWithDB
class AdminUserTests {

    @Autowired
    private lateinit var adminUserApi: AdminUserApi

    @Autowired
    private lateinit var authFixtureBuilder: AuthFixtureBuilder

    @Autowired
    private lateinit var userFixtureBuilder: UserFixtureBuilder

    @BeforeEach
    fun initAdmin() {
        val admin = userFixtureBuilder.buildUser(
            email = "admin@project.test",
            phone = "71234567890",
            password = "12345678",
            role = Role.SUPERADMIN
        )
        adminUserApi.accessToken = authFixtureBuilder.authorizeUser(admin.email, "12345678").accessToken
    }

    private val baseCreateUserBody = CreateUserRequestBody(
        email = "test-user@sasa.ee",
        phone = "77000000000",
        password = "12345678",
        role = RoleProperty.uSER
    )

    @Test
    fun createUserTest() {
        val (response, createUserBody) = createUser()
        assertEquals(createUserBody.email, response.email)
        assertEquals(createUserBody.phone, response.phone)
        assertEquals(createUserBody.role, response.role)
    }

    @Test
    fun updateUserTest() {
        val (response, createUserBody) = createUser()
        val updateUserBody = UpdateUserRequestBody(
            email = "test-user111@sasa.ee"
        )
        adminUserApi.updateUserRequest<UpdateUserRequestBody, Any>(
            userId = UUID.fromString(response.id),
            body = ApiClient.RequestBody(updateUserBody)
        )

        assertThrows<NotFoundException> {
            authFixtureBuilder.authorizeUser(
                createUserBody.email,
                createUserBody.password
            )
        }
        assertNotNull(updateUserBody.email)
        assertDoesNotThrow { authFixtureBuilder.authorizeUser(updateUserBody.email, createUserBody.password) }

        val (otherUser) = createUser()

        adminUserApi.updateUserRequest<UpdateUserRequestBody, ResponseList<ErrorDetail>>(
            userId = UUID.fromString(response.id),
            body = ApiClient.RequestBody(
                UpdateUserRequestBody(
                    email = otherUser.email,
                ),
            ),
            statusMatcher = { isUnprocessableEntity() }
        )
    }

    @Test
    fun getUserDataTest() {
        val (response, createUserBody) = createUser()
        val userProfile = adminUserApi.getUserRequest<UserInfo>(userId = UUID.fromString(response.id))
        assertEquals(response.id, userProfile.data.id)
        assertEquals(createUserBody.email, userProfile.data.email)
        assertEquals(createUserBody.phone, userProfile.data.phone)
        assertEquals(createUserBody.role, userProfile.data.role)
        assertEquals(createUserBody.isTest, userProfile.data.isTest)
        assertEquals(StatusProperty.aCTIVE, userProfile.data.status)
        assertNotNull(userProfile.data.createdAt)
        assertNotNull(userProfile.data.updatedAt)
    }

    @Test
    fun deleteUserTest() {
        val (response, createUserBody) = createUser()
        adminUserApi.deleteUserRequest<Any>(userId = UUID.fromString(response.id))
        assertThrows<NotFoundException> {
            authFixtureBuilder.authorizeUser(
                createUserBody.email,
                createUserBody.password
            )
        }
    }

    @Test
    fun blockUserTest() {
        val (response, createUserBody) = createUser()
        adminUserApi.blockUserRequest<Any>(userId = UUID.fromString(response.id))
        assertThrows<LockedException> {
            authFixtureBuilder.authorizeUser(
                response.email,
                createUserBody.password
            )
        }
    }

    @Test
    fun unblockUserTest() {
        val (response, createUserBody) = createUser()
        adminUserApi.blockUserRequest<Any>(userId = UUID.fromString(response.id))
        assertThrows<LockedException> {
            authFixtureBuilder.authorizeUser(
                createUserBody.email,
                createUserBody.password
            )
        }
        adminUserApi.unblockUserRequest<Any>(userId = UUID.fromString(response.id))
        assertDoesNotThrow { authFixtureBuilder.authorizeUser(createUserBody.email, createUserBody.password) }
    }

    @Test
    fun getUserListPaginationTest() {
        createUser()
        val userList = adminUserApi.getUserListRequest<ResponseList<UserInfo>>(withPagination = {
            Pagination(page = 0, size = 1)
        })
        assertEquals(1, userList.data.items.count())
        val userList1 = adminUserApi.getUserListRequest<ResponseList<UserInfo>>(withPagination = {
            Pagination(page = 1, size = 1)
        })
        assertEquals(1, userList1.data.items.count())
    }

    @Test
    fun getUserListTest() {
        val (response) = createUser()
        val userList = adminUserApi.getUserListRequest<ResponseList<UserInfo>> {
            mapOf("role" to listOf(RoleProperty.uSER.value))
        }
        assertContains(userList.data.items.map { it.id }, response.id)
    }

    private fun createUser(): Pair<UserInfo, CreateUserRequestBody> {
        val body = baseCreateUserBody.copy(
            email = HashUtils.generateRandomString(5) + baseCreateUserBody.email,
            phone = "7" + HashUtils.generateRandomCode(10)
        )
        val response = adminUserApi.createUserRequest<CreateUserRequestBody, UserInfo>(
            ApiClient.RequestBody(body)
        ).data
        assertDoesNotThrow { authFixtureBuilder.authorizeUser(body.email, body.password) }
        return response to body
    }
}
