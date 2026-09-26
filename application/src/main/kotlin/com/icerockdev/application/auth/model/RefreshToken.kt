package com.icerockdev.application.auth.model

import com.icerockdev.application.auth.dto.RefreshTokenDto
import com.icerockdev.application.common.Constants
import com.icerockdev.auth.acl.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "refresh_token")
class RefreshToken(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    val id: UUID,

    @Column(name = "token", nullable = false, length = Constants.MAX_TOKEN)
    val token: String,

    @Column(name = "entity_id", nullable = false)
    val entityId: UUID,

    @Column(name = "entity_type", nullable = false)
    @Enumerated(EnumType.STRING)
    val entityType: Role,

    @Column(name = "expired_at", nullable = false)
    val expiredAt: LocalDateTime
) {

    fun toDto() = RefreshTokenDto(
        id = id,
        token = token,
        entityId = entityId,
        entityType = entityType,
        expiredAt = expiredAt
    )
}
