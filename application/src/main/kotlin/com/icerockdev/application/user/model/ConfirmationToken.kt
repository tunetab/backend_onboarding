package com.icerockdev.application.user.model

import com.icerockdev.application.auth.enum.ConfirmationTokenType
import com.icerockdev.application.common.Constants
import com.icerockdev.application.user.dto.ConfirmationTokenDto
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
@Table(name = "confirmation_token")
class ConfirmationToken(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    val id: UUID,

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    val type: ConfirmationTokenType,

    @Column(name = "token", nullable = false, length = Constants.MAX_TOKEN)
    val token: String,

    @Column(name = "entity_id", nullable = false)
    val entityId: UUID,

    @Column(name = "entity_type", nullable = false)
    @Enumerated(EnumType.STRING)
    val entityType: Role,

    @Column(name = "phone", nullable = true, length = Constants.MAX_PHONE)
    val phone: String?,

    @Column(name = "expired_at", nullable = false)
    val expiredAt: LocalDateTime
) {
    fun toDto() = ConfirmationTokenDto(
        id = id,
        type = type,
        token = token,
        entityId = entityId,
        entityType = entityType,
        phone = phone,
        expiredAt = expiredAt
    )
}
