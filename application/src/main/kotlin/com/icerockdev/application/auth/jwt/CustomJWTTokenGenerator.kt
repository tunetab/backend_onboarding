package com.icerockdev.application.auth.jwt

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.icerockdev.application.auth.config.JwtSettings
import com.icerockdev.auth.acl.Role
import com.icerockdev.auth.jwt.JWTTokenGenerator
import com.icerockdev.auth.jwt.JwtConfig
import com.icerockdev.auth.jwt.JwtTokens
import com.icerockdev.auth.user.UserKey
import com.icerockdev.serialization.applyDefaultConfiguration
import java.util.UUID
import org.springframework.stereotype.Component

@Component
class CustomJWTTokenGenerator(
    jwtSettings: JwtSettings
) : JWTTokenGenerator<UserKey>(
    JwtConfig(
        secret = jwtSettings.secret,
        issuer = jwtSettings.issuer,
        audience = jwtSettings.audience,
        accessTokenTtl = jwtSettings.accessTokenTtl,
        refreshTokenTtl = jwtSettings.refreshTokenTtl
    ),
    jacksonObjectMapper().apply {
        applyDefaultConfiguration()
    }
) {
    fun makeTokens(userId: UUID, role: Role): JwtTokens {
        return generateTokens(user = UserKey(id = userId, role = role))
    }
}
