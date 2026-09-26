package com.icerockdev.application.user.dto

import com.icerockdev.application.auth.enum.ConfirmationTokenType
import com.icerockdev.application.user.model.ConfirmationToken
import com.icerockdev.auth.acl.Role
import java.time.LocalDateTime
import java.util.UUID

data class ConfirmationTokenDto(
    val id: UUID = UUID.randomUUID(),
    val type: ConfirmationTokenType,
    val token: String,
    val entityId: UUID,
    val entityType: Role,
    val phone: String?,
    val expiredAt: LocalDateTime
) {
    fun toEntity() = ConfirmationToken(
        id = id,
        type = type,
        token = token,
        entityId = entityId,
        entityType = entityType,
        phone = phone,
        expiredAt = expiredAt
    )
}
