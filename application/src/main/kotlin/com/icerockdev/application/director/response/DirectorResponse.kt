package com.icerockdev.application.director.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.icerockdev.application.director.dto.DirectorDto
import java.util.UUID

data class DirectorResponse(
    val id: UUID,
    val name: String,
    val country: String,
    @get:JsonProperty("win_oscar")
    val winOscar: Boolean,
    @get:JsonProperty("oscar_winner_title")
    val oscarWinnerTitle: String?,
    val createdAt: String,
    val updatedAt: String,
) {
    constructor(dto: DirectorDto) : this(
        id = dto.id,
        name = dto.name,
        country = dto.country,
        winOscar = dto.winOscar,
        oscarWinnerTitle = dto.oscarWinnerTitle,
        createdAt = dto.createdAt.toString(),
        updatedAt = dto.updatedAt.toString(),
    )
}
