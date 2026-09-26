package com.icerockdev.application.user.request

import com.icerockdev.application.user.dto.UserProfileDto
import com.icerockdev.i18n.LK
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.UUID

class UpdateUserSelfProfileRequest(
    @field:NotNull(message = "{${LK.userProfileFirstNameIsRequired}}")
    val firstName: String,
    val lastName: String? = null,
    val middleName: String? = null,
) {
    fun getUserProfileDto(userId: UUID) = UserProfileDto(
        userId = userId,
        firstName = firstName,
        lastName = lastName,
        middleName = middleName,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
}
