package com.icerockdev.application.user.usecase.base.impl

import com.icerockdev.application.auth.service.RefreshTokenService
import com.icerockdev.application.user.usecase.base.AfterDeleteUserUseCase
import com.icerockdev.auth.revoke.IRevokeTokenService
import com.icerockdev.auth.user.UserKey
import org.springframework.stereotype.Component

@Component
class AfterDeleteUserUseCaseImpl(
    private val refreshTokenService: RefreshTokenService,
    private val revokeTokenService: IRevokeTokenService<UserKey>
) : AfterDeleteUserUseCase {
    override fun invoke(input: AfterDeleteUserUseCase.Input) {
        refreshTokenService.deleteAll(
            entityId = input.credentials.id,
            userType = input.credentials.role
        )
        revokeTokenService.putRevoked(input.credentials, System.currentTimeMillis())
    }
}
