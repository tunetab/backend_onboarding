package com.icerockdev.application.director.dto

import com.icerockdev.application.director.model.Director
import java.time.LocalDateTime
import java.util.UUID

data class DirectorDto(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val country: String,
    val winOscar: Boolean,
    val oscarWinnerTitle: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    fun toEntity() = Director(
        id = id,
        name = name,
        country = country,
        winOscar = winOscar,
        oscarWinnerTitle = oscarWinnerTitle,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
