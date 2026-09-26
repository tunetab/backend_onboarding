package com.icerockdev.file.dto

import java.time.LocalDateTime
import java.util.UUID

data class FileDto(
    val key: String,
    val entityId: UUID? = null,
    val entityType: Int? = null,
    val meta: FileMetaDto? = null,
    val imageUrl: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
