package com.icerockdev.application.user.usecase.admin

import com.icerockdev.application.user.dto.CreateUserDto
import com.icerockdev.auth.user.UserKey
import com.icerockdev.domain.UseCase
import java.util.UUID

interface CreateUserUseCase : UseCase<CreateUserUseCase.Input, UUID> {

    data class Input(
        val createDto: CreateUserDto,
        val credentials: UserKey
    )
}
