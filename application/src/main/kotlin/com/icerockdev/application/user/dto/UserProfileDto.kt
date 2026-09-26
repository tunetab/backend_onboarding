package com.icerockdev.application.user.dto

import com.icerockdev.application.user.model.UserProfile
import java.time.LocalDateTime
import java.util.UUID

data class UserProfileDto(
    val userId: UUID,
    val firstName: String,
    val lastName: String?,
    val middleName: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime? = null,
) {

    fun toEntity() = UserProfile(
        userId = userId,
        firstName = firstName,
        lastName = lastName,
        middleName = middleName,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt
    )
}
