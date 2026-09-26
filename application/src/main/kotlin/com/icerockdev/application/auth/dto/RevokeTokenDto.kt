package com.icerockdev.application.auth.dto

import com.icerockdev.application.auth.model.RevokeToken
import com.icerockdev.auth.acl.Role
import java.time.LocalDateTime
import java.util.UUID

data class RevokeTokenDto(
    val id: UUID = UUID.randomUUID(),
    val entityId: UUID,
    val entityType: Role,
    val expiredAt: LocalDateTime
) {
    fun toEntity() = RevokeToken(
        id = id,
        entityId = entityId,
        entityType = entityType,
        expiredAt = expiredAt
    )
}
