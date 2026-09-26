package com.icerockdev.application.auth.model

import com.icerockdev.application.auth.dto.RevokeTokenDto
import com.icerockdev.auth.acl.Role
import java.util.UUID
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "revoke_token")
class RevokeToken(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    val id: UUID,

    @Column(name = "entity_id", nullable = false)
    val entityId: UUID,

    @Column(name = "entity_type", nullable = false)
    @Enumerated(EnumType.STRING)
    val entityType: Role,

    @Column(name = "expired_at", nullable = false)
    val expiredAt: LocalDateTime
) {
    fun toDto() = RevokeTokenDto(
        id = id,
        entityId = entityId,
        entityType = entityType,
        expiredAt = expiredAt
    )
}
