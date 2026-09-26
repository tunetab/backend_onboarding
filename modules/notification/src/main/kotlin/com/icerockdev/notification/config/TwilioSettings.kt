package com.icerockdev.notification.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "twilio")
class TwilioSettings {
    lateinit var phone: String
    lateinit var accountSid: String
    lateinit var authToken: String
}
