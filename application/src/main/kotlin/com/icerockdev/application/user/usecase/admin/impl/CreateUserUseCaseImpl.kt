package com.icerockdev.application.user.usecase.admin.impl

import com.icerockdev.application.user.dto.UserDto
import com.icerockdev.application.user.service.UserService
import com.icerockdev.application.user.usecase.admin.CreateUserUseCase
import com.icerockdev.auth.acl.Role
import com.icerockdev.exception.ForbiddenException
import com.icerockdev.exception.ValidationException
import com.icerockdev.i18n.I18N
import com.icerockdev.i18n.LK
import com.icerockdev.util.HashUtils
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class CreateUserUseCaseImpl(
    private val userService: UserService,
    private val i18N: I18N
) : CreateUserUseCase {
    override fun invoke(input: CreateUserUseCase.Input): UUID {
        if (input.credentials.role !in listOf(Role.ADMIN, Role.SUPERADMIN)) {
            throw ForbiddenException(i18N.t(LK.actionNotAllowed))
        }

        if (userService.existByEmail(email = input.createDto.email)) {
            throw ValidationException(message = i18N.t(LK.alreadyExistEmail))
        }

        val now: LocalDateTime = LocalDateTime.now()
        val userDto = UserDto(
            phone = input.createDto.phone,
            email = input.createDto.email,
            role = input.createDto.role,
            status = input.createDto.status,
            isTest = input.createDto.isTest,
            password = HashUtils.generatePasswordHash(input.createDto.password),
            createdAt = now,
            updatedAt = now
        )

        return userService.save(userDto).id
    }
}
