package com.icerockdev.notification.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "fcm")
class FcmSettings {
    lateinit var googleServiceAccountJson: String
}
