package com.icerockdev.notification.controller

import com.icerockdev.api.response.EmptyResponse
import com.icerockdev.api.response.Response
import com.icerockdev.api.response.ResponseList
import com.icerockdev.auth.getUserKey
import com.icerockdev.auth.user.UserKey
import com.icerockdev.notification.dto.NotificationSettingsDto
import com.icerockdev.notification.model.NotificationSettings
import com.icerockdev.notification.request.PushTokenRequest
import com.icerockdev.notification.request.UpdateNotificationSettingsRequest
import com.icerockdev.notification.response.NotificationResponse
import com.icerockdev.notification.response.NotificationSettingsResponse
import com.icerockdev.notification.usecase.AddTokenUseCase
import com.icerockdev.notification.usecase.CreateNotificationSettingsUseCase
import com.icerockdev.notification.usecase.DeleteNotificationUseCase
import com.icerockdev.notification.usecase.GetNotificationListUseCase
import com.icerockdev.notification.usecase.GetNotificationSettingsResponseListUseCase
import com.icerockdev.notification.usecase.GetNotificationSettingsUseCase
import com.icerockdev.notification.usecase.RemoveTokenUseCase
import com.icerockdev.notification.usecase.SetAllNotificationIsReadByUserUseCase
import com.icerockdev.notification.usecase.SetNotificationAsReadUseCase
import com.icerockdev.notification.usecase.UpdateNotificationSettingsUseCase
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import java.util.UUID
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


/**
 * Class for work with FCM push token
 */
@Validated
@RestController
@RequestMapping("/v1/notification")
internal class NotificationController(
    private val addTokenUseCase: AddTokenUseCase,
    private val removeTokenUseCase: RemoveTokenUseCase,
    private val getNotificationListUseCase: GetNotificationListUseCase,
    private val setAllNotificationIsReadByUserUseCase: SetAllNotificationIsReadByUserUseCase,
    private val setNotificationAsReadUseCase: SetNotificationAsReadUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase,
    private val getNotificationSettingsResponseListUseCase: GetNotificationSettingsResponseListUseCase,
    private val getNotificationSettingsUseCase: GetNotificationSettingsUseCase,
    private val updateNotificationSettingsUseCase: UpdateNotificationSettingsUseCase,
    private val createNotificationSettingsUseCase: CreateNotificationSettingsUseCase
) {
    @PostMapping("/token")
    fun addPushToken(
        @RequestBody
        @Valid
        request: PushTokenRequest,
        authentication: Authentication,
    ): ResponseEntity<*> {
        addTokenUseCase.execute(
            entityId = authentication.getUserKey().id,
            token = request.token
        )

        return ResponseEntity.ok(EmptyResponse())
    }

    @DeleteMapping("/token/{token}")
    fun deletePushToken(
        @Valid
        @PathVariable(name = "token")
        token: String
    ): ResponseEntity<*> {
        removeTokenUseCase.execute(token)

        return ResponseEntity.ok(EmptyResponse())
    }

    @GetMapping("")
    fun getList(
        pagination: Pageable,
        @Valid
        authentication: Authentication,
    ): ResponseEntity<*> {
        val user = authentication.getUserKey()
        val notifications: List<NotificationResponse> = getNotificationListUseCase.execute(
            entityId = user.id,
            entityType = user.role,
            pagination = pagination
        )

        return ResponseEntity.ok(
            Response(
                data = ResponseList(
                    items = notifications,
                    totalElements = notifications.count(),
                    totalPages = 0
                )
            )
        )
    }

    @PutMapping("/{notificationId}/read")
    fun setIsRead(
        @PathVariable(name = "notificationId")
        @NotBlank
        @org.hibernate.validator.constraints.UUID
        notificationId: String,
        authentication: Authentication
    ): ResponseEntity<*> {
        val user = authentication.getUserKey()

        setNotificationAsReadUseCase.execute(
            entityId = user.id,
            entityType = user.role,
            notificationId = UUID.fromString(notificationId)
        )

        return ResponseEntity.ok(EmptyResponse())
    }

    @PutMapping("/read")
    fun setAllIsRead(
        @RequestParam("type") type: String?,
        authentication: Authentication
    ): ResponseEntity<*> {
        val user = authentication.getUserKey()

        setAllNotificationIsReadByUserUseCase.execute(
            entityId = user.id,
            entityType = user.role,
            type = type?.toInt()
        )

        return ResponseEntity.ok(EmptyResponse())
    }

    @DeleteMapping("/{notificationId}")
    fun deleteNotification(
        authentication: Authentication,
        @Validated
        @PathVariable(name = "notificationId")
        @NotBlank
        @org.hibernate.validator.constraints.UUID
        notificationId: String
    ): ResponseEntity<*> {
        val user: UserKey = authentication.getUserKey()
        deleteNotificationUseCase.execute(
            entityId = user.id,
            entityType = user.role,
            notificationId = UUID.fromString(notificationId)
        )

        return ResponseEntity.ok(EmptyResponse())
    }

    @GetMapping("/settings")
    fun getSettings(
        @Valid
        authentication: Authentication,
    ): ResponseEntity<*> {
        val user: UserKey = authentication.getUserKey()
        val settingsResponseList: List<NotificationSettingsResponse> =
            getNotificationSettingsResponseListUseCase.execute(
                entityId = user.id,
                entityType = user.role
            )

        return ResponseEntity.ok(
            Response(
                data = ResponseList(
                    items = settingsResponseList,
                    totalElements = settingsResponseList.count(),
                    totalPages = 0,
                )
            )
        )
    }

    @PutMapping("/settings")
    fun updateSettings(
        @Valid
        @RequestBody
        request: UpdateNotificationSettingsRequest,
        authentication: Authentication,
    ): ResponseEntity<*> {
        val user: UserKey = authentication.getUserKey()
        val settings: NotificationSettings? = getNotificationSettingsUseCase.execute(
            entityId = user.id,
            entityType = user.role,
            type = request.type
        )

        val response: NotificationSettingsResponse? = settings?.let {
            updateNotificationSettingsUseCase.execute(
                entityType = user.role,
                notificationSettingsDto = request.fillDto(notificationSettingsDto = settings.getDto())
            )
        } ?: createNotificationSettingsUseCase.execute(
            NotificationSettingsDto(
                id = UUID.randomUUID(),
                entityId = user.id,
                entityType = user.role,
                type = request.type,
                enabled = request.enabled
            )
        )

        return ResponseEntity.ok(Response(data = response))
    }
}
