package com.icerockdev.application.user.usecase.user.impl

import com.icerockdev.application.user.service.UserProfileService
import com.icerockdev.application.user.service.UserService
import com.icerockdev.application.user.usecase.base.AfterDeleteUserUseCase
import com.icerockdev.application.user.usecase.user.DeleteSelfAccountUseCase
import com.icerockdev.auth.acl.Role
import com.icerockdev.exception.ForbiddenException
import com.icerockdev.i18n.I18N
import com.icerockdev.i18n.LK
import org.springframework.stereotype.Component

@Component
class DeleteSelfAccountUseCaseImpl(
    private val userService: UserService,
    private val userProfileService: UserProfileService,
    private val afterDeleteUserUseCase: AfterDeleteUserUseCase,
    private val i18N: I18N
) : DeleteSelfAccountUseCase {
    override fun invoke(input: DeleteSelfAccountUseCase.Input) {
        if (input.credentials.role == Role.SUPERADMIN) {
            throw ForbiddenException(i18N.t(LK.actionNotAllowed))
        }

        userService.delete(input.credentials.id)
        userProfileService.delete(input.credentials.id)
        afterDeleteUserUseCase.invoke(AfterDeleteUserUseCase.Input(input.credentials))
    }
}
