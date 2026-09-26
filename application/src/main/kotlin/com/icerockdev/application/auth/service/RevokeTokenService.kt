package com.icerockdev.application.auth.service

import com.icerockdev.application.auth.dto.RevokeTokenDto
import com.icerockdev.application.auth.repository.RevokeTokenRepository
import com.icerockdev.auth.acl.Role
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class RevokeTokenService(
    private val revokeTokenRepository: RevokeTokenRepository
) {
    fun save(revokeTokenDto: RevokeTokenDto): RevokeTokenDto {
        return revokeTokenRepository
            .save(revokeTokenDto.toEntity())
            .toDto()
    }

    fun getAllNotExpired(): List<RevokeTokenDto> {
        return revokeTokenRepository
            .getAllNotExpired()
            .map { it.toDto() }
    }

    fun deleteAllExpired(expiredAt: LocalDateTime) = revokeTokenRepository.deleteAllExpired(expiredAt)

    fun findByEntityIdAndEntityType(entityId: UUID, entityType: Role): RevokeTokenDto? {
        return revokeTokenRepository
            .findByEntityIdAndEntityType(entityId, entityType)
            ?.toDto()
    }
}
