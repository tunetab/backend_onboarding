package com.icerockdev.application.user.response

import com.icerockdev.application.user.dto.UserProfileDto
import java.util.UUID

class UserProfileResponse(
    val userId: UUID,
    val firstName: String,
    val lastName: String?,
    val middleName: String?,
    val createdAt: String,
    val updatedAt: String
) {
    constructor(dto: UserProfileDto) : this(
        userId = dto.userId,
        firstName = dto.firstName,
        lastName = dto.lastName,
        middleName = dto.middleName,
        createdAt = dto.createdAt.toString(),
        updatedAt = dto.updatedAt.toString()
    )
}
