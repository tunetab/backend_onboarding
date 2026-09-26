package com.icerockdev.notification.usecase

import com.icerockdev.notification.dto.SettingsForSaveNotification
import com.icerockdev.service.email.MailerService
import org.springframework.stereotype.Component

/**
 * Sends notification on email
 */
@Component
internal class SendEmailUseCase(
    private val mailerService: MailerService,
    private val saveNotificationUseCase: SaveNotificationUseCase,
) {

    fun execute(
        subject: String,
        to: Map<String, String?>,
        emailBody: String,
        async: Boolean,
        projectFromEmail: String?,
        projectFromName: String?,
        settingsForSaveNotification: SettingsForSaveNotification?
    ) {
        val mail = mailerService.compose().apply {
            this.fromEmail = projectFromEmail ?: ""
            this.fromName = projectFromName ?: ""
            this.subject = subject
            this.to = to.toMutableMap()
            this.html = emailBody
        }

        when {
            async -> mail.sendAsync()
            else -> mail.send()
        }

        if (settingsForSaveNotification != null) {
            saveNotificationUseCase.execute(
                entityId = settingsForSaveNotification.entityId,
                entityType = settingsForSaveNotification.entityType,
                message = emailBody,
                type = settingsForSaveNotification.notificationType
            )
        }
    }
}
