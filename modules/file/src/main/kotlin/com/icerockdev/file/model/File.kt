package com.icerockdev.file.model

import com.icerockdev.file.dto.FileMetaDto
import com.vladmihalcea.hibernate.type.json.JsonType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID
import org.hibernate.annotations.Type

@Entity
@Table(name = "file")
internal data class File(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    val id: UUID,

    @Column(name = "key", nullable = false, length = 50)
    val key: String,

    @Column(name = "entity_id", nullable = true)
    val entityId: UUID? = null,

    @Column(name = "entity_type", nullable = true, length = 50)
    val entityType: Int? = null,

    @Type(JsonType::class)
    @Column(name = "meta", columnDefinition = "jsonb", nullable = true)
    val meta: FileMetaDto? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime
)
