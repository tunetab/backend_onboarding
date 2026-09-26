package com.icerockdev.application.user.usecase.base

import com.icerockdev.auth.user.UserKey
import com.icerockdev.domain.UseCase

interface AfterDeleteUserUseCase : UseCase<AfterDeleteUserUseCase.Input, Unit> {
    data class Input(
        val credentials: UserKey
    )
}
