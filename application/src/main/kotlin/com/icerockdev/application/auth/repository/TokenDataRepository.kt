package com.icerockdev.application.auth.repository

import com.icerockdev.application.auth.dto.RevokeTokenDto
import com.icerockdev.application.auth.jwt.CustomJWTTokenGenerator
import com.icerockdev.application.auth.service.RevokeTokenService
import com.icerockdev.auth.revoke.ITokenDataRepository
import com.icerockdev.auth.user.UserKey
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.TimeZone

@Component
class TokenDataRepository(
    private val jwtTokenGenerator: CustomJWTTokenGenerator,
    private val revokeTokenService: RevokeTokenService
) : ITokenDataRepository<UserKey> {

    override fun getAllNotExpired(): Map<UserKey, Long> {
        return revokeTokenService.getAllNotExpired().associate {
            UserKey(it.entityId, it.entityType) to it.expiredAt.toInstant(ZoneOffset.ofTotalSeconds(0))
                .toEpochMilli()
        }
    }

    override fun cleanUp() {
        revokeTokenService.deleteAllExpired(
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(System.currentTimeMillis() + jwtTokenGenerator.getAccessTokenTtl()),
                TimeZone.getDefault().toZoneId()
            )
        )
    }

    override fun insertOrUpdate(key: UserKey, revokeAt: Long): Boolean {
        val expiredAt: LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(revokeAt), TimeZone.getDefault().toZoneId())
        val revokeTokenDto: RevokeTokenDto = revokeTokenService
            .findByEntityIdAndEntityType(key.id, key.role)
            ?: RevokeTokenDto(
                entityId = key.id,
                entityType = key.role,
                expiredAt = expiredAt
            )
        revokeTokenService.save(revokeTokenDto)

        return true
    }
}
