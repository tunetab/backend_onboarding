package com.icerockdev.application.user.usecase.admin.impl

import com.icerockdev.application.user.service.UserService
import com.icerockdev.application.user.usecase.admin.ChangeUserStatusUseCase
import com.icerockdev.application.user.usecase.admin.CheckAdminAccessUseCase
import java.time.LocalDateTime
import org.springframework.stereotype.Component

@Component
class ChangeUserStatusUseCaseImpl(
    private val userService: UserService,
    private val checkAdminAccessUseCase: CheckAdminAccessUseCase
) : ChangeUserStatusUseCase {
    override fun invoke(input: ChangeUserStatusUseCase.Input) {
        val userDto = userService.getById(input.id)

        checkAdminAccessUseCase.invoke(
            CheckAdminAccessUseCase.Input(
                userDto = userDto,
                credentials = input.credentials
            )
        )

        userService.save(
            userDto.copy(
                status = input.status,
                updatedAt = LocalDateTime.now()
            )
        )
    }
}
