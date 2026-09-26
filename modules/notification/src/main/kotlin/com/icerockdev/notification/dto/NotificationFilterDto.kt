package com.icerockdev.notification.dto

internal data class NotificationFilterDto(
    val userId: Int? = null,
    val userType: Int? = null,
    val type: Int? = null,
    val isRead: Boolean? = null
)
