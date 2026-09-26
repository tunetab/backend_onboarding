package com.icerockdev.application.tests.modules.notification

import com.icerockdev.api.response.ResponseList
import com.icerockdev.application.tests.common.api.client.ApiClient
import com.icerockdev.application.tests.common.api.client.pagination.Pagination
import com.icerockdev.application.tests.common.db.annotations.SpringBootTestWithDB
import com.icerockdev.application.tests.fixture.AuthFixtureBuilder
import com.icerockdev.application.tests.fixture.UserFixtureBuilder
import com.icerockdev.application.tests.modules.notification.api.NotificationApi
import com.icerockdev.application.user.model.User
import com.icerockdev.auth.acl.Role
import com.icerockdev.notification.abstract.INotificationTypeListByUserType
import com.icerockdev.notification.tests.fixture.NotificationFixtureBuilder
import com.icerockdev.notification.tests.fixture.NotificationSettingsFixtureBuilder
import com.icerockdev.notification.tests.fixture.TestNotificationType
import com.icerockdev.util.HashUtils
import kotlin.test.assertContains
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openapitools.client.models.AddPushTokenRequestBody
import org.openapitools.client.models.Notification
import org.openapitools.client.models.NotificationSettings
import org.openapitools.client.models.UpdateNotificationSettingsRequestBody
import org.springframework.beans.factory.annotation.Autowired

@SpringBootTestWithDB
class NotificationApiTests {
    private lateinit var user: User

    @Autowired
    private lateinit var notificationApi: NotificationApi

    @Autowired
    private lateinit var authFixtureBuilder: AuthFixtureBuilder

    @Autowired
    private lateinit var userFixtureBuilder: UserFixtureBuilder

    @Autowired
    private lateinit var notificationFixtureBuilder: NotificationFixtureBuilder

    @Autowired
    private lateinit var notificationSettingsFixtureBuilder: NotificationSettingsFixtureBuilder

    @Autowired
    private lateinit var testNotificationTypeListByUserType: INotificationTypeListByUserType

    @BeforeEach
    fun initUser() {
        user = userFixtureBuilder.buildUser(
            email = "user@project.test",
            phone = "71234567890",
            password = "12345678",
            role = Role.USER
        )
        notificationApi.accessToken = authFixtureBuilder.authorizeUser(user.email, "12345678").accessToken
    }

    @Test
    fun addPushTokenTest() {
        notificationApi.addPushTokenRequest<AddPushTokenRequestBody, Any?>(
            ApiClient.RequestBody(
                AddPushTokenRequestBody(token = HashUtils.generateRandomString(100))
            )
        )
    }

    @Test
    fun deletePushTokenTest() {
        val token = HashUtils.generateRandomString(100)
        notificationApi.addPushTokenRequest<AddPushTokenRequestBody, Any?>(
            ApiClient.RequestBody(
                AddPushTokenRequestBody(token = token)
            )
        )
        notificationApi.deletePushTokenRequest<Any?>(token)
    }

    @Test
    fun notificationListTest() {
        val notification = notificationFixtureBuilder.buildNotification(
            entityId = user.id,
            entityType = Role.USER,
            message = "Test"
        )

        val list = notificationApi.getNotificationListRequest<ResponseList<Notification>>()
        assertEquals(1, list.data.items.count())
        assertContains(list.data.items.map { it.id }, notification.toString())
    }

    @Test
    fun notificationListWithPaginationTest() {
        val notificationOne = notificationFixtureBuilder.buildNotification(
            entityId = user.id,
            entityType = Role.USER,
            message = "Test"
        )

        val notificationTwo = notificationFixtureBuilder.buildNotification(
            entityId = user.id,
            entityType = Role.USER,
            message = "Test"
        )

        val list = notificationApi.getNotificationListRequest<ResponseList<Notification>> {
            Pagination(page = 0, size = 1)
        }
        assertEquals(1, list.data.items.count())
        assertContains(list.data.items.map { it.id }, notificationOne.toString())

        val listTwo = notificationApi.getNotificationListRequest<ResponseList<Notification>> {
            Pagination(page = 1, size = 1)
        }
        assertEquals(1, list.data.items.count())
        assertContains(listTwo.data.items.map { it.id }, notificationTwo.toString())
    }

    @Test
    fun setNotificationAsReadTest() {
        val notification = notificationFixtureBuilder.buildNotification(
            entityId = user.id,
            entityType = Role.USER,
            message = "Test"
        )
        notificationApi.setNotificationAsReadRequest<Any>(notification.toString())
        val list = notificationApi.getNotificationListRequest<ResponseList<Notification>> {
            Pagination(page = 0, size = 1)
        }
        assertEquals(1, list.data.items.count())
        assertContains(list.data.items.map { it.id }, notification.toString())
        assertContains(list.data.items.map { it.isRead }, true)
    }

    @Test
    fun setAllNotificationAsReadTest() {
        val notificationOne = notificationFixtureBuilder.buildNotification(
            entityId = user.id,
            entityType = Role.USER,
            message = "Test"
        )

        val notificationTwo = notificationFixtureBuilder.buildNotification(
            entityId = user.id,
            entityType = Role.USER,
            message = "Test"
        )
        notificationApi.setAllNotificationAsReadRequest<Any?>()
        val list = notificationApi.getNotificationListRequest<ResponseList<Notification>>()
        assertEquals(2, list.data.items.count())
        assertContains(list.data.items.map { it.id }, notificationOne.toString())
        assertContains(list.data.items.map { it.id }, notificationTwo.toString())
        assertEquals(list.data.items.map { it.isRead }, listOf(true, true))
    }

    @Test
    fun deleteNotificationTest() {
        val notification = notificationFixtureBuilder.buildNotification(
            entityId = user.id,
            entityType = Role.USER,
            message = "Test"
        )
        notificationApi.deleteNotificationRequest<Any>(notification.toString())
        val list = notificationApi.getNotificationListRequest<ResponseList<Notification>>()
        assertEquals(0, list.data.totalElements)
    }

    @Test
    fun notificationSettingsListTest() {
        val list = notificationApi.getNotificationSettingsRequest<ResponseList<NotificationSettings>>()
        assertEquals(
            testNotificationTypeListByUserType.getList(Role.USER).count(),
            list.data.items.count()
        )
    }

    @Test
    fun updateNotificationSettingsTest() {
        notificationSettingsFixtureBuilder.buildNotificationSettings(
            entityId = user.id,
            entityType = Role.USER,
            type = TestNotificationType.INFO,
            enabled = false
        )

        notificationApi.updateNotificationSettingsRequest<UpdateNotificationSettingsRequestBody, Any?>(
            ApiClient.RequestBody(
                UpdateNotificationSettingsRequestBody(
                    type = TestNotificationType.INFO.getType().toBigDecimal(),
                    enabled = true
                )
            )
        )

        val list = notificationApi.getNotificationSettingsRequest<ResponseList<NotificationSettings>>()
        assertEquals(1, list.data.items.count())
        assertEquals(true, list.data.items.first().enabled)
    }
}
