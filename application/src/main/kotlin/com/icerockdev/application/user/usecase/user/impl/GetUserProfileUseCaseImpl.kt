package com.icerockdev.application.user.usecase.user.impl

import com.icerockdev.application.user.dto.UserProfileDto
import com.icerockdev.application.user.service.UserProfileService
import com.icerockdev.application.user.service.UserService
import com.icerockdev.application.user.usecase.user.GetUserProfileUseCase
import org.springframework.stereotype.Component

@Component
class GetUserProfileUseCaseImpl(
    private val userService: UserService,
    private val userProfileService: UserProfileService
) : GetUserProfileUseCase {
    override fun invoke(input: GetUserProfileUseCase.Input): UserProfileDto? {
        val user = userService.getById(input.credentials.id)
        return userProfileService.findById(user.id)
    }
}
