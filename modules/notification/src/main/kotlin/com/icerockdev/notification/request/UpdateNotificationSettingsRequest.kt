package com.icerockdev.notification.request

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.icerockdev.api.request.BaseRequest
import com.icerockdev.notification.dto.NotificationSettingsDto
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
internal class UpdateNotificationSettingsRequest(
    @field:NotNull(message = "type is required")
    val type: Int,
    @field:NotNull(message = "enabled is required")
    val enabled: Boolean,
) : BaseRequest() {
    @JsonIgnore
    fun fillDto(notificationSettingsDto: NotificationSettingsDto): NotificationSettingsDto {
        return notificationSettingsDto.copy(
            enabled = enabled,
            updatedAt = LocalDateTime.now()
        )
    }
}
