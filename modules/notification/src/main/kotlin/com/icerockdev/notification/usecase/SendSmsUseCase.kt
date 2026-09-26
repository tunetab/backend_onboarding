package com.icerockdev.notification.usecase

import com.icerockdev.notification.dto.SettingsForSaveNotification
import com.icerockdev.service.sms.SmsService
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component

/**
 * Sends notification via SMS
 */
@Component
internal class SendSmsUseCase(
    private val smsService: SmsService,
    private val saveNotificationUseCase: SaveNotificationUseCase
) {

    fun execute(
        message: String,
        phone: String,
        settingsForSaveNotification: SettingsForSaveNotification? = null
    ) {
        runBlocking {
            smsService.send(
                to = String.format("+%s", phone),
                text = message
            )
        }

        if (settingsForSaveNotification != null) {
            saveNotificationUseCase.execute(
                entityId = settingsForSaveNotification.entityId,
                entityType = settingsForSaveNotification.entityType,
                message = message,
                type = settingsForSaveNotification.notificationType
            )
        }
    }
}
