package com.icerockdev.application.auth.controller

import com.icerockdev.api.response.AbstractResponse
import com.icerockdev.api.response.SuccessResponse
import com.icerockdev.auth.getUserKey
import com.icerockdev.application.auth.request.SignOutRequest
import com.icerockdev.application.auth.usecase.SignOutUseCase
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(("/v1/auth"))
class LogOutController(
    private val signOutUseCase: SignOutUseCase
) {

    @PostMapping("/logout")
    fun logout(
        @RequestBody
        @Valid
        request: SignOutRequest,
        authentication: Authentication,
    ): ResponseEntity<AbstractResponse> {
        signOutUseCase.execute(authentication.getUserKey(), request.refreshToken)
        return ResponseEntity.ok(SuccessResponse())
    }
}
