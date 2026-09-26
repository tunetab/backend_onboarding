package com.icerockdev.notification.abstract

import com.icerockdev.i18n.I18N

interface INotificationType {
    fun getType(): Int
    fun getLabel(i18n: I18N): String
}
