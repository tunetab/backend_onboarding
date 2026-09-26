package com.icerockdev.application.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import kotlin.properties.Delegates

@Configuration
@ConfigurationProperties(prefix = "jwt")
class JwtSettings {
    lateinit var secret: String
    lateinit var audience: String
    lateinit var issuer: String
    var accessTokenTtl by Delegates.notNull<Long>()
    var refreshTokenTtl by Delegates.notNull<Long>()
}
