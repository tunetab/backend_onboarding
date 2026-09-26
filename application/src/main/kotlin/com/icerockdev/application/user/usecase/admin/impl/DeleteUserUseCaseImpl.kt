package com.icerockdev.application.user.usecase.admin.impl

import com.icerockdev.application.user.service.UserProfileService
import com.icerockdev.application.user.service.UserService
import com.icerockdev.application.user.usecase.admin.CheckAdminAccessUseCase
import com.icerockdev.application.user.usecase.admin.DeleteUserUseCase
import com.icerockdev.application.user.usecase.base.AfterDeleteUserUseCase
import com.icerockdev.auth.user.UserKey
import org.springframework.stereotype.Component

@Component
class DeleteUserUseCaseImpl(
    private val userService: UserService,
    private val userProfileService: UserProfileService,
    private val afterDeleteUserUseCase: AfterDeleteUserUseCase,
    private val checkAdminAccessUseCase: CheckAdminAccessUseCase
) : DeleteUserUseCase {
    override fun invoke(input: DeleteUserUseCase.Input) {
        val userDto = userService.getById(input.id)

        checkAdminAccessUseCase.invoke(
            CheckAdminAccessUseCase.Input(
                userDto = userDto,
                credentials = input.credentials
            )
        )

        userService.delete(input.id)
        userProfileService.delete(input.id)
        afterDeleteUserUseCase.invoke(
            AfterDeleteUserUseCase.Input(
                UserKey(
                    userDto.id,
                    userDto.role
                )
            )
        )
    }
}
