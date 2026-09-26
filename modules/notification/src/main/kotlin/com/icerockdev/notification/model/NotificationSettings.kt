package com.icerockdev.notification.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.icerockdev.auth.acl.Role
import com.icerockdev.notification.dto.NotificationSettingsDto
import java.util.UUID
import java.time.LocalDateTime
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "notification_settings")
internal data class NotificationSettings(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "entity_id", nullable = false)
    val entityId: UUID,

    @Column(name = "entity_type", nullable = false)
    @Enumerated(EnumType.STRING)
    val entityType: Role,

    @Column(name = "type", nullable = false)
    val type: Int,

    @Column(name = "enabled", nullable = false)
    val enabled: Boolean,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime
) {
    constructor(dto: NotificationSettingsDto) : this(
        entityId = dto.entityId,
        entityType = dto.entityType,
        type = dto.type,
        enabled = dto.enabled,
        createdAt = dto.createdAt,
        updatedAt = dto.updatedAt
    )

    @JsonIgnore
    fun getDto(): NotificationSettingsDto {
        return NotificationSettingsDto(
            id = id,
            entityId = entityId,
            entityType = entityType,
            type = type,
            enabled = enabled,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
