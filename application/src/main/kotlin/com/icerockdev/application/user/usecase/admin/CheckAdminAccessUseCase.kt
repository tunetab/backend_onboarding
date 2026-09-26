package com.icerockdev.application.user.usecase.admin

import com.icerockdev.application.user.dto.UserDto
import com.icerockdev.auth.user.UserKey
import com.icerockdev.domain.UseCase

interface CheckAdminAccessUseCase : UseCase<CheckAdminAccessUseCase.Input, Unit> {

    data class Input(
        val userDto: UserDto,
        val credentials: UserKey
    )
}
