package com.icerockdev.application.user.usecase.admin.impl

import com.icerockdev.application.user.dto.UserDto
import com.icerockdev.application.user.service.UserService
import com.icerockdev.application.user.usecase.admin.CheckAdminAccessUseCase
import com.icerockdev.application.user.usecase.admin.UpdateUserUseCase
import com.icerockdev.exception.ErrorDetail
import com.icerockdev.exception.ValidationException
import com.icerockdev.i18n.I18N
import com.icerockdev.i18n.LK
import com.icerockdev.util.HashUtils
import java.time.LocalDateTime
import org.springframework.stereotype.Component

@Component
class UpdateUserUseCaseImpl(
    private val userService: UserService,
    private val checkAdminAccessUseCase: CheckAdminAccessUseCase,
    private val i18N: I18N
) : UpdateUserUseCase {
    override fun invoke(input: UpdateUserUseCase.Input): UserDto {
        val userDto: UserDto = userService.getById(input.id)

        checkAdminAccessUseCase.invoke(
            CheckAdminAccessUseCase.Input(
                userDto = userDto,
                credentials = input.credentials
            )
        )

        if (
            input.updateDto.email != null &&
            input.updateDto.email != userDto.email &&
            userService.existByEmail(input.updateDto.email)
        ) {
            throw ValidationException(
                listOf(
                    ErrorDetail(
                        message = i18N.t(LK.alreadyExistEmail),
                        field = "email",
                    )
                )
            )
        }

        val updatedUserDto: UserDto = userDto.copy(
            email = input.updateDto.email ?: userDto.email,
            phone = input.updateDto.phone ?: userDto.phone,
            password = input.updateDto.password?.let { HashUtils.generatePasswordHash(it) } ?: userDto.password,
            role = input.updateDto.role ?: userDto.role,
            status = input.updateDto.status ?: userDto.status,
            isTest = input.updateDto.isTest ?: userDto.isTest,
            updatedAt = LocalDateTime.now()
        )

        return userService.save(updatedUserDto)
    }
}
