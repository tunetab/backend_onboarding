package com.icerockdev.application.user.usecase.admin.impl

import com.icerockdev.application.user.dto.UserDto
import com.icerockdev.application.user.service.UserService
import com.icerockdev.application.user.usecase.admin.GetUserUseCase
import com.icerockdev.auth.acl.Role
import com.icerockdev.exception.ForbiddenException
import com.icerockdev.i18n.I18N
import com.icerockdev.i18n.LK
import org.springframework.stereotype.Component

@Component
class GetUserUseCaseImpl(
    private val userService: UserService,
    private val i18N: I18N
) : GetUserUseCase {
    override fun invoke(input: GetUserUseCase.Input): UserDto {
        if (input.credentials.role !in listOf(Role.ADMIN, Role.SUPERADMIN)) {
            throw ForbiddenException(i18N.t(LK.actionNotAllowed))
        }

        return userService.getById(input.id)
    }
}
