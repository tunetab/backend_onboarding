package com.icerockdev.application.user.usecase.admin.impl

import com.icerockdev.application.user.usecase.admin.CheckAdminAccessUseCase
import com.icerockdev.auth.acl.Role
import com.icerockdev.exception.ForbiddenException
import com.icerockdev.i18n.I18N
import com.icerockdev.i18n.LK
import org.springframework.stereotype.Component

@Component
class CheckAdminAccessUseCaseImpl(
    private val i18N: I18N
) : CheckAdminAccessUseCase {
    override fun invoke(input: CheckAdminAccessUseCase.Input) {
        if (!input.credentials.role.isAdmin()) {
            throw ForbiddenException(i18N.t(LK.actionNotAllowed))
        }

        if (input.userDto.id == input.credentials.id) {
            throw ForbiddenException(i18N.t(LK.actionNotAllowed))
        }

        if (input.userDto.role == Role.SUPERADMIN) {
            throw ForbiddenException(i18N.t(LK.actionNotAllowed))
        }
    }
}
