package com.icerockdev.application.user.usecase.user

import com.icerockdev.auth.user.UserKey
import com.icerockdev.domain.UseCase

interface DeleteSelfAccountUseCase : UseCase<DeleteSelfAccountUseCase.Input, Unit> {
    data class Input(
        val credentials: UserKey
    )
}
