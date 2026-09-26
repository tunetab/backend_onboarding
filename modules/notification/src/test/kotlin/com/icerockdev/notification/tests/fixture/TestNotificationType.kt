package com.icerockdev.notification.tests.fixture

import com.icerockdev.i18n.I18N
import com.icerockdev.notification.abstract.INotificationType

enum class TestNotificationType(private val type: Int, private val label: String) : INotificationType {
    INFO(1, "information");

    override fun getType(): Int {
        return type
    }

    override fun getLabel(i18n: I18N): String {
        return label
    }
}
