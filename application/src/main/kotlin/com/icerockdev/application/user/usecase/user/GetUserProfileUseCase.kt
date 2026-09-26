package com.icerockdev.application.user.usecase.user

import com.icerockdev.application.user.dto.UserProfileDto
import com.icerockdev.auth.user.UserKey
import com.icerockdev.domain.UseCase

interface GetUserProfileUseCase : UseCase<GetUserProfileUseCase.Input, UserProfileDto?> {

    data class Input(
        val credentials: UserKey
    )
}
