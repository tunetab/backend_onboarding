package com.icerockdev.application.user.usecase.admin

import com.icerockdev.application.user.dto.UserDto
import com.icerockdev.auth.user.UserKey
import com.icerockdev.domain.UseCase
import java.util.UUID

interface GetUserUseCase : UseCase<GetUserUseCase.Input, UserDto> {

    data class Input(
        val id: UUID,
        val credentials: UserKey
    )
}
