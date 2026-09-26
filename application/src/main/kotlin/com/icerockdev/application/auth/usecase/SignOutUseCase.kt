package com.icerockdev.application.auth.usecase

import com.icerockdev.application.auth.dto.RefreshTokenDto
import com.icerockdev.application.auth.service.RefreshTokenService
import com.icerockdev.auth.revoke.IRevokeTokenService
import com.icerockdev.auth.user.UserKey
import org.springframework.stereotype.Component

@Component
class SignOutUseCase(
    private val refreshTokenService: RefreshTokenService,
    private val revokeTokenService: IRevokeTokenService<UserKey>
) {

    fun execute(userKey: UserKey, refreshToken: String): Boolean {
        val refreshTokenDto: RefreshTokenDto = refreshTokenService.getByTokenAndUserType(
            refreshToken, userKey.role
        ) ?: return false

        refreshTokenService.delete(refreshTokenDto)
        revokeTokenService.putRevoked(UserKey(refreshTokenDto.entityId, userKey.role), System.currentTimeMillis())

        return true
    }
}
