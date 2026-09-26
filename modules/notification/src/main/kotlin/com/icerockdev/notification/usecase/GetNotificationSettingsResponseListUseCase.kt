package com.icerockdev.notification.usecase

import com.icerockdev.auth.acl.Role
import com.icerockdev.i18n.I18N
import com.icerockdev.notification.abstract.INotificationTypeListByUserType
import com.icerockdev.notification.common.Constants
import com.icerockdev.notification.response.NotificationSettingsResponse
import com.icerockdev.notification.service.NotificationSettingsService
import java.util.UUID
import org.springframework.stereotype.Component

@Component
internal class GetNotificationSettingsResponseListUseCase(
    private val notificationSettingsService: NotificationSettingsService,
    private val getINotificationTypeListByUserType: INotificationTypeListByUserType,
    private val i18n: I18N
) {
    fun execute(
        entityId: UUID,
        entityType: Role
    ): List<NotificationSettingsResponse> {
        val notificationTypeList = getINotificationTypeListByUserType.getList(entityType = entityType)
        val notificationSettingsMap = notificationSettingsService.getAllByEntityIdAndEntityType(
            entityId = entityId,
            entityType = entityType
        ).associateBy { it.type }

        return notificationTypeList.map { notificationType ->
            notificationSettingsMap[notificationType.getType()]?.let { notificationSettings ->
                NotificationSettingsResponse(
                    notificationSettings = notificationSettings,
                    label = notificationTypeList.firstOrNull {
                        it.getType() == notificationSettings.type
                    }?.getLabel(i18n) ?: ""
                )
            } ?: NotificationSettingsResponse(
                type = notificationType.getType(),
                enabled = Constants.DEFAULT_NOTIFICATION_SETTING_ENABLED_VALUE,
                label = notificationType.getLabel(i18n)
            )
        }
    }
}
