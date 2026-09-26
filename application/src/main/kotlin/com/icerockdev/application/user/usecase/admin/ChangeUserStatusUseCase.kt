package com.icerockdev.application.user.usecase.admin

import com.icerockdev.application.user.model.Status
import com.icerockdev.auth.user.UserKey
import com.icerockdev.domain.UseCase
import java.util.UUID

interface ChangeUserStatusUseCase : UseCase<ChangeUserStatusUseCase.Input, Unit> {

    data class Input(
        val id: UUID,
        val status: Status,
        val credentials: UserKey
    )
}
