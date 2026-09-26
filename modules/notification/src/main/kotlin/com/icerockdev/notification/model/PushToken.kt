package com.icerockdev.notification.model

import com.icerockdev.notification.common.Constants
import com.icerockdev.notification.dto.PushTokenDto
import java.util.UUID
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "push_tokens")
internal data class PushToken(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    val id: UUID,

    @Column(name = "entity_id", nullable = false)
    val entityId: UUID,

    @Column(name = "token", nullable = false, length = Constants.MAX_TOKEN)
    val token: String
) {
    constructor(dto: PushTokenDto) : this(
        id = dto.id,
        token = dto.token,
        entityId = dto.entityId
    )
}
