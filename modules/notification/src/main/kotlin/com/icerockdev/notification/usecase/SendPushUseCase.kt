package com.icerockdev.notification.usecase

import com.icerockdev.notification.dto.NotificationModuleData
import com.icerockdev.notification.dto.SettingsForSaveNotification
import com.icerockdev.notification.service.PushTokenService
import com.icerockdev.service.fcmpush.FCMPayLoad
import com.icerockdev.service.fcmpush.NotificationData
import com.icerockdev.service.fcmpush.PushService
import java.util.UUID
import org.springframework.stereotype.Component

/**
 * Sends notification via FCM
 */
@Component
internal class SendPushUseCase(
    private val pushService: PushService,
    private val pushTokenService: PushTokenService,
    private val saveNotificationUseCase: SaveNotificationUseCase,
) {

    fun execute(
        userIds: List<UUID>,
        notificationData: NotificationModuleData,
        data: Map<String, String>? = null,
        settingsForSaveNotification: SettingsForSaveNotification? = null
    ) {
        val tokens: List<String> = pushTokenService.getTokensByUserIds(userIds).map {
            it.token
        }

        if (tokens.isEmpty()) {
            return
        }

        val notification = NotificationData(
            title = notificationData.title,
            body = notificationData.body,
            sound = notificationData.sound,
            clickAction = notificationData.clickAction,
            badge = notificationData.badge,
            icon = notificationData.icon,
            color = notificationData.color,
            tag = notificationData.tag
        )

        pushService.sendAsync(
            payLoad = FCMPayLoad(
                dataObject = data,
                notificationObject = notification,
                tokenList = tokens
            )
        )

        if (settingsForSaveNotification != null) {
            saveNotificationUseCase.execute(
                entityId = settingsForSaveNotification.entityId,
                entityType = settingsForSaveNotification.entityType,
                message = notification.body,
                type = settingsForSaveNotification.notificationType
            )
        }
    }
}
