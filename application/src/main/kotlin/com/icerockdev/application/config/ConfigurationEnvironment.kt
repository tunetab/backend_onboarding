package com.icerockdev.application.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ConfigurationEnvironment(
    @Value("\${api.base-url}")
    private val apiUrl: String,
    @Value("\${api.admin-url}")
    private val adminUrl: String
) {

    fun getConfirmationTokenUrl(confirmationToken: String): String = "${apiUrl}/v1/auth/restore/$confirmationToken"
    fun getAdminConfirmationTokenUrl(confirmationToken: String): String = "${adminUrl}/restore/$confirmationToken"
    fun getAdminUrl() = adminUrl
}
