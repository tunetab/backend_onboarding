package com.icerockdev.i18n

import java.util.Locale
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource

class I18N(private val source: ResourceBundleMessageSource) {
    fun t(key: String, params: List<String>? = null, locale: Locale? = null): String {
        return source.getMessage(key, params?.toTypedArray(), locale ?: getLocale())
    }

    private fun getLocale(): Locale {
        return LocaleContextHolder.getLocale()
    }
}
