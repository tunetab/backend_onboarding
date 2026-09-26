package com.icerockdev.application.user.controller

import com.icerockdev.api.response.AbstractResponse
import com.icerockdev.api.response.Response
import com.icerockdev.api.response.SuccessResponse
import com.icerockdev.application.user.request.UpdateUserSelfProfileRequest
import com.icerockdev.application.user.response.UserProfileResponse
import com.icerockdev.application.user.usecase.user.DeleteSelfAccountUseCase
import com.icerockdev.application.user.usecase.user.GetUserProfileUseCase
import com.icerockdev.application.user.usecase.user.UpdateUserProfileUseCase
import com.icerockdev.auth.getUserKey
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/v1/user")
class UserController(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val deleteSelfAccountUseCase: DeleteSelfAccountUseCase
) {

    @GetMapping("/profile")
    fun getProfile(
        authentication: Authentication
    ): ResponseEntity<*> {
        val profileDto = getUserProfileUseCase.invoke(GetUserProfileUseCase.Input(authentication.getUserKey()))

        return ResponseEntity.ok(Response(data = profileDto?.let { UserProfileResponse(it) }))
    }

    @PutMapping("/profile")
    fun updateProfile(
        @RequestBody
        @Valid
        request: UpdateUserSelfProfileRequest,
        authentication: Authentication
    ): ResponseEntity<AbstractResponse> {
        updateUserProfileUseCase.invoke(
            UpdateUserProfileUseCase.Input(
                request.getUserProfileDto(authentication.getUserKey().id)
            )
        )

        return ResponseEntity.ok(SuccessResponse())
    }

    @DeleteMapping("")
    fun deleteSelfAccount(
        authentication: Authentication
    ): ResponseEntity<AbstractResponse> {
        deleteSelfAccountUseCase.invoke(DeleteSelfAccountUseCase.Input(authentication.getUserKey()))

        return ResponseEntity.ok(SuccessResponse())
    }
}
