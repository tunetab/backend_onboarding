package com.icerockdev.notification.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import kotlin.properties.Delegates

@Configuration
@ConfigurationProperties(prefix = "smtp")
class SMTPSettings {
    lateinit var host: String
    var port: Int by Delegates.notNull()
    lateinit var secure: String
    var auth: Boolean by Delegates.notNull()
    lateinit var user: String
    lateinit var password: String
    var from: FromData? = null

    class FromData {
        lateinit var email: String
        lateinit var name: String
    }
}
