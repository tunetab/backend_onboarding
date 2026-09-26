package com.icerockdev.notification.response

import com.icerockdev.notification.model.NotificationSettings

internal class NotificationSettingsResponse(
    val type: Int,
    val enabled: Boolean,
    val label: String
) {

    constructor(notificationSettings: NotificationSettings, label: String) : this(
        type = notificationSettings.type,
        enabled = notificationSettings.enabled,
        label = label
    )
}
