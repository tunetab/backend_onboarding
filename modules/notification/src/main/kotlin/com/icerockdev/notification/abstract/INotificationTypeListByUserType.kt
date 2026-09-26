package com.icerockdev.notification.abstract

import com.icerockdev.auth.acl.Role

interface INotificationTypeListByUserType {
    fun getList(entityType: Role): List<INotificationType>
}
