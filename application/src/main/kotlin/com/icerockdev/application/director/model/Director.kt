package com.icerockdev.application.director.model

import com.icerockdev.application.director.dto.DirectorDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "directors")
class Director(
    @Id
    @Column(nullable = false, updatable = false)
    val id: UUID,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val country: String,

    @Column(name = "win_oscar", nullable = false)
    val winOscar: Boolean,

    @Column(name = "oscar_winner_title")
    val oscarWinnerTitle: String?,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime,
) {
    fun toDto() = DirectorDto(
        id = id,
        name = name,
        country = country,
        winOscar = winOscar,
        oscarWinnerTitle = oscarWinnerTitle,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
