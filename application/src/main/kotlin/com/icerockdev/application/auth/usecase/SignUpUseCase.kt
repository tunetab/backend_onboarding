package com.icerockdev.application.auth.usecase

import com.icerockdev.application.auth.dto.RefreshTokenDto
import com.icerockdev.application.auth.jwt.CustomJWTTokenGenerator
import com.icerockdev.application.auth.request.SignUpRequest
import com.icerockdev.application.auth.service.RefreshTokenService
import com.icerockdev.application.user.dto.UserDto
import com.icerockdev.application.user.service.UserService
import com.icerockdev.auth.jwt.JwtTokens
import com.icerockdev.exception.ValidationException
import com.icerockdev.i18n.I18N
import com.icerockdev.i18n.LK
import com.icerockdev.util.HashUtils
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Component
class SignUpUseCase(
    private val refreshTokenService: RefreshTokenService,
    private val userService: UserService,
    private val jwtTokenGenerator: CustomJWTTokenGenerator,
    private val i18N: I18N
) {
    @Transactional
    fun execute(request: SignUpRequest): JwtTokens {
        val userDto: UserDto = request.getDto()

        if (userService.existByEmail(email = userDto.email)) {
            throw ValidationException(i18N.t(LK.alreadyExistEmail))
        }

        if (userService.existByPhone(phone = userDto.phone)) {
            throw ValidationException(i18N.t(LK.alreadyExistPhone))
        }

        val savedUserDto: UserDto = userService.save(
            userDto.copy(password = HashUtils.generatePasswordHash(userDto.password))
        )

        val jwtTokens: JwtTokens = jwtTokenGenerator.makeTokens(userId = savedUserDto.id, role = savedUserDto.role)
        RefreshTokenDto(
            token = jwtTokens.refreshToken,
            entityId = savedUserDto.id,
            entityType = savedUserDto.role,
            expiredAt = LocalDateTime.now().plus(jwtTokenGenerator.getRefreshTokenTtl(), ChronoUnit.MILLIS)
        )
            .also { refreshTokenService.save(it) }

        return jwtTokens
    }
}
