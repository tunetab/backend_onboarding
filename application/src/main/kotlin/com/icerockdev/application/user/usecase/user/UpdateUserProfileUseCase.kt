package com.icerockdev.application.user.usecase.user

import com.icerockdev.application.user.dto.UserProfileDto
import com.icerockdev.domain.UseCase

interface UpdateUserProfileUseCase : UseCase<UpdateUserProfileUseCase.Input, UserProfileDto> {
    data class Input(
        val userProfileDto: UserProfileDto
    )
}
