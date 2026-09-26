package com.icerockdev.notification.tests.fixture

import com.icerockdev.auth.acl.Role
import com.icerockdev.notification.abstract.INotificationType
import com.icerockdev.notification.abstract.INotificationTypeListByUserType

class TestNotificationTypeListByUserType : INotificationTypeListByUserType {
    override fun getList(entityType: Role): List<INotificationType> {
        return when (entityType) {
            Role.USER -> listOf(TestNotificationType.INFO)
            else -> emptyList()
        }
    }
}
