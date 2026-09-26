package com.icerockdev.application.auth.usecase

import com.icerockdev.application.auth.dto.RefreshTokenDto
import com.icerockdev.application.auth.jwt.CustomJWTTokenGenerator
import com.icerockdev.application.auth.request.SignInByPhoneRequest
import com.icerockdev.application.auth.request.SignInRequest
import com.icerockdev.application.auth.service.RefreshTokenService
import com.icerockdev.application.user.dto.UserDto
import com.icerockdev.application.user.service.UserService
import com.icerockdev.auth.acl.Role
import com.icerockdev.auth.jwt.JwtTokens
import com.icerockdev.exception.NotFoundException
import com.icerockdev.i18n.I18N
import com.icerockdev.i18n.LK
import com.icerockdev.util.HashUtils
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Component
class SignInUseCase(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val refreshTokenService: RefreshTokenService,
    private val jwtTokenGenerator: CustomJWTTokenGenerator,
    private val i18N: I18N
) {
    fun execute(request: SignInRequest): JwtTokens {
        val userDto: UserDto = userService.getByEmail(email = request.email)

        if (!HashUtils.verifyPassword(request.password, userDto.password)) {
            throw NotFoundException(i18N.t(LK.userNotFound))
        }

        return authorize(userDto, request.password)
    }

    fun execute(request: SignInByPhoneRequest): JwtTokens {
        val userDto: UserDto = userService.getByPhone(phone = request.phone)

        if (!HashUtils.verifyPassword(request.password, userDto.password)) {
            throw NotFoundException(i18N.t(LK.userNotFound))
        }

        return authorize(userDto, request.password)
    }

    private fun authorize(userDto: UserDto, password: String): JwtTokens {
        val jwtTokens: JwtTokens = jwtTokenGenerator.makeTokens(userId = userDto.id, role = userDto.role)

        RefreshTokenDto(
            token = jwtTokens.refreshToken,
            entityId = userDto.id,
            entityType = Role.valueOf(userDto.role.name),
            expiredAt = LocalDateTime.now().plus(jwtTokenGenerator.getAccessTokenTtl(), ChronoUnit.MILLIS)
        )
            .also { refreshTokenService.save(it) }

        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                userDto.email,
                password
            )
        )

        return jwtTokens
    }
}
