package com.icerockdev.application.tests.application.config

import com.icerockdev.application.auth.abstract.INotificationService
import com.icerockdev.notification.NotificationService
import com.icerockdev.notification.dto.NotificationModuleData
import com.icerockdev.notification.dto.SettingsForSaveNotification
import java.util.UUID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
open class ApplicationTestConfiguration {
    @Bean
    @Primary
    open fun getTestINotificationService(): INotificationService {
        return object : INotificationService {
            @Autowired
            private lateinit var notificationService: NotificationService
            override fun sendEmail(
                subject: String,
                to: Map<String, String?>,
                emailBody: String,
                async: Boolean,
                settingsForSaveNotification: INotificationService.ApplicationSettingsForSaveNotification?
            ) = notificationService.sendEmail(
                subject = subject,
                to = to,
                emailBody = emailBody,
                async = async,
                settingsForSaveNotification = settingsForSaveNotification?.let {
                    SettingsForSaveNotification(
                        entityId = it.entityId,
                        entityType = it.entityType,
                        notificationType = it.notificationType
                    )
                }
            )

            override fun sendPush(
                userIds: List<UUID>,
                notification: INotificationService.ApplicationNotificationModuleData,
                data: Map<String, String>?,
                settingsForSaveNotification: INotificationService.ApplicationSettingsForSaveNotification?
            ) = notificationService.sendPush(
                userIds = userIds,
                notification = NotificationModuleData(
                    title = notification.title,
                    body = notification.body,
                    sound = notification.sound,
                    clickAction = notification.clickAction,
                    badge = notification.badge,
                    icon = notification.icon,
                    color = notification.color,
                    tag = notification.tag
                ),
                data = data,
                settingsForSaveNotification = settingsForSaveNotification?.let {
                    SettingsForSaveNotification(
                        entityId = it.entityId,
                        entityType = it.entityType,
                        notificationType = it.notificationType
                    )
                }
            )

            override fun sendSms(
                message: String,
                phone: String,
                settingsForSaveNotification: INotificationService.ApplicationSettingsForSaveNotification?
            ) = notificationService.sendSms(
                message = message,
                phone = phone,
                settingsForSaveNotification = settingsForSaveNotification?.let {
                    SettingsForSaveNotification(
                        entityId = it.entityId,
                        entityType = it.entityType,
                        notificationType = it.notificationType
                    )
                }
            )
        }
    }
}
