package com.icerockdev.application.auth.service

import com.icerockdev.application.auth.enum.ConfirmationTokenType
import com.icerockdev.application.user.dto.ConfirmationTokenDto
import com.icerockdev.application.user.model.ConfirmationToken
import com.icerockdev.application.user.repository.ConfirmationTokenRepository
import com.icerockdev.auth.acl.Role
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class ConfirmationTokenService(
    private val confirmationTokenRepository: ConfirmationTokenRepository
) {
    fun save(confirmationTokenDto: ConfirmationTokenDto): ConfirmationTokenDto {
        return confirmationTokenRepository
            .save(confirmationTokenDto.toEntity())
            .toDto()
    }

    fun existsByTokenAndType(token: String, type: ConfirmationTokenType): Boolean =
        confirmationTokenRepository.existsByTokenAndType(token, type)

    fun getByPhone(token: String, phone: String, type: ConfirmationTokenType): ConfirmationTokenDto? {
        val now: LocalDateTime = LocalDateTime.now()
        return confirmationTokenRepository
            .findByTokenAndPhoneAndTypeAndExpiredAtIsGreaterThanEqual(
                token = token,
                phone = phone,
                type = type,
                date = now
            )
            ?.toDto()
    }

    fun getByTokenAndType(token: String, type: ConfirmationTokenType): ConfirmationTokenDto? {
        return confirmationTokenRepository
            .findByTokenAndTypeAndExpiredAtIsGreaterThanEqual(token, type, LocalDateTime.now())
            ?.toDto()
    }

    fun deleteAllByEntityIdAndEntityTypeAndType(entityId: UUID, entityType: Role, type: ConfirmationTokenType) =
        confirmationTokenRepository.deleteAllByEntityIdAndEntityTypeAndType(entityId, entityType, type)

    fun getByEntityIdAndEntityTypeAndPhoneAndType(
        entityId: UUID,
        entityType: Role,
        phone: String?,
        type: ConfirmationTokenType
    ): ConfirmationToken? {
        return confirmationTokenRepository.findByEntityIdAndEntityTypeAndPhoneAndType(entityId, entityType, phone, type)
    }
}
