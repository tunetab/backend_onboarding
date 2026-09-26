package com.icerockdev.notification.usecase

import com.icerockdev.i18n.I18N
import com.icerockdev.notification.abstract.INotificationTypeListByUserType
import com.icerockdev.notification.common.Constants
import com.icerockdev.notification.dto.NotificationSettingsDto
import com.icerockdev.notification.response.NotificationSettingsResponse
import com.icerockdev.notification.service.NotificationSettingsService
import org.springframework.stereotype.Component

@Component
internal class CreateNotificationSettingsUseCase(
    private val notificationSettingsService: NotificationSettingsService,
    private val getINotificationTypeListByUserType: INotificationTypeListByUserType,
    private val i18n: I18N
) {
    fun execute(dto: NotificationSettingsDto): NotificationSettingsResponse? {
        notificationSettingsService.insert(
            notificationSettingsDto = dto
        )
        val notificationType = getINotificationTypeListByUserType.getList(entityType = dto.entityType).firstOrNull {
            it.getType() == dto.type
        } ?: return null

        return NotificationSettingsResponse(
            type = notificationType.getType(),
            enabled = Constants.DEFAULT_NOTIFICATION_SETTING_ENABLED_VALUE,
            label = notificationType.getLabel(i18n)
        )
    }
}
