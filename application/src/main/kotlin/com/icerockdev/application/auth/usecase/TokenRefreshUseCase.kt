package com.icerockdev.application.auth.usecase

import com.icerockdev.application.auth.dto.RefreshTokenDto
import com.icerockdev.application.auth.jwt.CustomJWTTokenGenerator
import com.icerockdev.application.auth.service.RefreshTokenService
import com.icerockdev.application.user.dto.UserDto
import com.icerockdev.application.user.model.Status
import com.icerockdev.application.user.service.UserService
import com.icerockdev.auth.jwt.JwtTokens
import com.icerockdev.auth.user.UserKey
import com.icerockdev.exception.BadRequestException
import com.icerockdev.i18n.I18N
import com.icerockdev.i18n.LK
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import org.springframework.transaction.annotation.Transactional

@Component
class TokenRefreshUseCase(
    private val refreshTokenService: RefreshTokenService,
    private val jwtTokenGenerator: CustomJWTTokenGenerator,
    private val userService: UserService,
    private val i18N: I18N
) {
    @Transactional
    fun execute(token: String): JwtTokens {
        val userKey: UserKey = jwtTokenGenerator.getUserKey(token)
        val role = userKey.role
        val refreshTokenDto: RefreshTokenDto = refreshTokenService.getByTokenAndUserType(token, role)
            ?: throw BadRequestException(i18N.t(LK.authFailedRefreshToken))

        val userDto: UserDto = userService.getById(refreshTokenDto.entityId)
        if (userDto.id != userKey.id) {
            throw BadRequestException(i18N.t(LK.authFailedRefreshToken))
        }

        if (userDto.status != Status.ACTIVE) {
            throw BadRequestException(i18N.t(LK.authFailedRefreshToken))
        }

        val jwtTokens: JwtTokens = jwtTokenGenerator.makeTokens(refreshTokenDto.entityId, role)

        val updatedRefreshTokenDto: RefreshTokenDto = refreshTokenDto.copy(
            token = jwtTokens.refreshToken,
            expiredAt = LocalDateTime.now().plus(jwtTokenGenerator.getRefreshTokenTtl(), ChronoUnit.MILLIS)
        )

        refreshTokenService.deleteAllExpired(refreshTokenDto.entityId, role)

        refreshTokenService.save(updatedRefreshTokenDto)

        return jwtTokens
    }
}
