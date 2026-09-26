package com.icerockdev.application.auth.service

import com.icerockdev.application.auth.dto.RefreshTokenDto
import com.icerockdev.application.auth.repository.RefreshTokenRepository
import com.icerockdev.auth.acl.Role
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID


@Service
class RefreshTokenService(
    private val refreshTokenRepository: RefreshTokenRepository
) {
    fun save(refreshTokenDto: RefreshTokenDto): RefreshTokenDto {
        return refreshTokenRepository
            .save(refreshTokenDto.toEntity())
            .toDto()
    }

    fun getByTokenAndUserType(token: String, userType: Role): RefreshTokenDto? {
        return refreshTokenRepository
            .findByTokenAndEntityType(token, userType)
            ?.toDto()
    }

    fun delete(refreshTokenDto: RefreshTokenDto) = refreshTokenRepository.delete(refreshTokenDto.toEntity())

    fun deleteAllExpired(entityId: UUID, userType: Role) {
        refreshTokenRepository.deleteAllByEntityIdAndEntityTypeAndExpiredAtIsLessThanEqual(
            entityId = entityId,
            entityType = userType,
            date = LocalDateTime.now()
        )
    }

    fun deleteAll(entityId: UUID, userType: Role) {
        refreshTokenRepository.deleteAllByEntityIdAndEntityType(
            entityId = entityId,
            entityType = userType
        )
    }
}
