package com.icerockdev.notification

import com.icerockdev.notification.config.SMTPSettings
import com.icerockdev.notification.dto.NotificationModuleData
import com.icerockdev.notification.dto.SettingsForSaveNotification
import com.icerockdev.notification.usecase.CheckIsEnabledByNotificationSettingsUseCase
import com.icerockdev.notification.usecase.SendEmailUseCase
import com.icerockdev.notification.usecase.SendPushUseCase
import com.icerockdev.notification.usecase.SendSmsUseCase
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class NotificationService(
    private val smtpSettings: SMTPSettings
) {
    @Autowired
    private lateinit var sendEmailUseCase: SendEmailUseCase

    @Autowired
    private lateinit var sendPushUseCase: SendPushUseCase

    @Autowired
    private lateinit var sendSmsUseCase: SendSmsUseCase

    @Autowired
    private lateinit var checkIsEnabled: CheckIsEnabledByNotificationSettingsUseCase

    fun sendEmail(
        subject: String,
        to: Map<String, String?>,
        emailBody: String,
        async: Boolean,
        settingsForSaveNotification: SettingsForSaveNotification? = null
    ) {
        if (!checkIsNotificationEnabled(settingsForSaveNotification)) {
            return
        }

        sendEmailUseCase.execute(
            subject = subject,
            to = to,
            emailBody = emailBody,
            async = async,
            settingsForSaveNotification = settingsForSaveNotification,
            projectFromEmail = smtpSettings.from?.email,
            projectFromName = smtpSettings.from?.name
        )
    }

    fun sendPush(
        userIds: List<UUID>,
        notification: NotificationModuleData,
        data: Map<String, String>? = null,
        settingsForSaveNotification: SettingsForSaveNotification? = null
    ) {
        if (!checkIsNotificationEnabled(settingsForSaveNotification)) {
            return
        }

        sendPushUseCase.execute(
            userIds = userIds,
            notificationData = notification,
            data = data,
            settingsForSaveNotification = settingsForSaveNotification
        )
    }

    fun sendSms(
        message: String,
        phone: String,
        settingsForSaveNotification: SettingsForSaveNotification? = null
    ) {
        if (!checkIsNotificationEnabled(settingsForSaveNotification)) {
            return
        }

        sendSmsUseCase.execute(
            message = message,
            phone = phone,
            settingsForSaveNotification = settingsForSaveNotification
        )
    }

    private fun checkIsNotificationEnabled(settingsForSaveNotification: SettingsForSaveNotification?): Boolean {
        return settingsForSaveNotification?.let { settings ->
            settings.notificationType?.let {
                checkIsEnabled.execute(settings.entityId, settings.entityType, it)
            }
        } ?: true
    }
}
