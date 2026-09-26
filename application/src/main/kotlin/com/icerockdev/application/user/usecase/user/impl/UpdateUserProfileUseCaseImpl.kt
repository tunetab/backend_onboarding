package com.icerockdev.application.user.usecase.user.impl

import com.icerockdev.application.user.dto.UserProfileDto
import com.icerockdev.application.user.service.UserProfileService
import com.icerockdev.application.user.usecase.user.UpdateUserProfileUseCase
import org.springframework.stereotype.Component

@Component
class UpdateUserProfileUseCaseImpl(
    private val userProfileService: UserProfileService
) : UpdateUserProfileUseCase {
    override fun invoke(input: UpdateUserProfileUseCase.Input): UserProfileDto {
        return userProfileService.save(input.userProfileDto)
    }
}
