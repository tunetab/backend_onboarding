package com.icerockdev.application.auth.abstract

import com.icerockdev.auth.acl.Role
import java.util.UUID

interface INotificationService {
    fun sendEmail(
        subject: String,
        to: Map<String, String?>,
        emailBody: String,
        async: Boolean,
        settingsForSaveNotification: ApplicationSettingsForSaveNotification? = null
    )

    fun sendPush(
        userIds: List<UUID>,
        notification: ApplicationNotificationModuleData,
        data: Map<String, String>? = null,
        settingsForSaveNotification: ApplicationSettingsForSaveNotification? = null
    )

    fun sendSms(
        message: String,
        phone: String,
        settingsForSaveNotification: ApplicationSettingsForSaveNotification? = null
    )

    /**
     * Промежуточный класс для конвертации в NotificationData внешнего модуля com.icerockdev.service.fcmpush
     * */
    data class ApplicationNotificationModuleData(
        /**
         * android/ios: notification title
         */
        val title: String,
        /**
         * android/ios: the body text is the main content of the notification
         */
        val body: String,
        /**
         * android/ios: can be default or a filename of a sound resource bundled in the app.
         */
        val sound: String? = null,
        /**
         * android/ios: what should happen upon notification click. when empty on android the default activity
         * will be launched passing any payload to an intent.
         */
        val clickAction: String? = null,
        /**
         * iOS only: will add small red bubbles indicating the number of notifications to your apps icon
         */
        val badge: String? = null,
        /**
         * android only: set the name of your drawable resource as string
         */
        val icon: String? = null,
        /**
         * android only: background color of the notification icon when showing details on notifications
         */
        val color: String? = null,
        /**
         * android only: when set notification will replace prior notifications from the same app with the same tag.
         */
        val tag: String? = null
    )

    /**
     * Промежуточный класс для конвертации в SettingsForSaveNotification модуля notification
     * */
    data class ApplicationSettingsForSaveNotification(
        val entityId: UUID,
        val entityType: Role,
        val notificationType: Int? = null
    )
}
