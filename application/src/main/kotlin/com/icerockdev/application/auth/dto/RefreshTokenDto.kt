package com.icerockdev.application.auth.dto

import com.icerockdev.application.auth.model.RefreshToken
import com.icerockdev.auth.acl.Role
import java.time.LocalDateTime
import java.util.UUID


data class RefreshTokenDto(
    val id: UUID = UUID.randomUUID(),
    val token: String,
    val entityId: UUID,
    val entityType: Role,
    val expiredAt: LocalDateTime
) {
    fun toEntity() = RefreshToken(
        id = id,
        token = token,
        entityId = entityId,
        entityType = entityType,
        expiredAt = expiredAt
    )
}
