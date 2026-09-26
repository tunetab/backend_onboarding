package com.icerockdev.notification.model

import com.icerockdev.auth.acl.Role
import com.icerockdev.notification.dto.NotificationDto
import java.util.UUID
import java.time.LocalDateTime
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "notifications")
internal data class Notification(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    val id: UUID,

    @Column(name = "entity_id", nullable = false)
    val entityId: UUID,

    @Column(name = "entity_type", nullable = false)
    @Enumerated(EnumType.STRING)
    val entityType: Role,

    @JvmField
    @Column(name = "message", nullable = false)
    val message: String,

    @Column(name = "type", nullable = true)
    val type: Int?,

    @Column(name = "is_read", nullable = false)
    val isRead: Boolean,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime
) {
    constructor(dto: NotificationDto) : this(
        id = dto.id,
        entityId = dto.entityId,
        entityType = dto.entityType,
        message = dto.message,
        type = dto.type,
        isRead = dto.isRead,
        createdAt = dto.createdAt,
        updatedAt = dto.updatedAt
    )
}
