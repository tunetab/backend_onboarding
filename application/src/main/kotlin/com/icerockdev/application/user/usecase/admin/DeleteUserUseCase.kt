package com.icerockdev.application.user.usecase.admin

import com.icerockdev.auth.user.UserKey
import com.icerockdev.domain.UseCase
import java.util.UUID

interface DeleteUserUseCase : UseCase<DeleteUserUseCase.Input, Unit> {

    data class Input(
        val id: UUID,
        val credentials: UserKey
    )
}
