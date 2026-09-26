package com.icerockdev.notification.usecase

import com.icerockdev.auth.acl.Role
import com.icerockdev.i18n.I18N
import com.icerockdev.notification.abstract.INotificationTypeListByUserType
import com.icerockdev.notification.dto.NotificationSettingsDto
import com.icerockdev.notification.response.NotificationSettingsResponse
import com.icerockdev.notification.service.NotificationSettingsService
import org.springframework.stereotype.Component

@Component
internal class UpdateNotificationSettingsUseCase(
    private val notificationSettingsService: NotificationSettingsService,
    private val getINotificationTypeListByUserType: INotificationTypeListByUserType,
    private val i18n: I18N
) {
    fun execute(entityType: Role, notificationSettingsDto: NotificationSettingsDto): NotificationSettingsResponse {
        val updatedSettings = notificationSettingsService.update(
            notificationSettingsDto = notificationSettingsDto
        )
        val notificationTypeList = getINotificationTypeListByUserType.getList(entityType = entityType)
        return NotificationSettingsResponse(
            notificationSettings = updatedSettings,
            label = notificationTypeList.firstOrNull { it.getType() == updatedSettings.type }?.getLabel(i18n) ?: ""
        )
    }
}
