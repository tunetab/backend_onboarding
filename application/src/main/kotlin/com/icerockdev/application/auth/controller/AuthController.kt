package com.icerockdev.application.auth.controller

import com.icerockdev.api.response.AbstractResponse
import com.icerockdev.api.response.SuccessResponse
import com.icerockdev.application.auth.request.ChangeRestorePasswordRequest
import com.icerockdev.application.auth.request.RefreshTokenRequest
import com.icerockdev.application.auth.request.RestorePasswordByEmailRequest
import com.icerockdev.application.auth.request.RestorePasswordByPhoneRequest
import com.icerockdev.application.auth.request.SignInByPhoneRequest
import com.icerockdev.application.auth.request.SignInRequest
import com.icerockdev.application.auth.request.SignUpRequest
import com.icerockdev.application.auth.response.TokenResponse
import com.icerockdev.application.auth.usecase.SignInUseCase
import com.icerockdev.application.auth.usecase.SignUpUseCase
import com.icerockdev.application.auth.usecase.TokenRefreshUseCase
import com.icerockdev.application.auth.usecase.UserRestorePasswordChangeUseCase
import com.icerockdev.application.auth.usecase.UserRestorePasswordUseCase
import com.icerockdev.application.common.Constants
import com.icerockdev.auth.jwt.JwtTokens
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val tokenRefreshUseCase: TokenRefreshUseCase,
    private val userRestorePasswordUseCase: UserRestorePasswordUseCase,
    private val userRestorePasswordChangeUseCase: UserRestorePasswordChangeUseCase
) {

    @PostMapping("/sign-up")
    fun signUp(
        @RequestBody
        @Valid
        request: SignUpRequest
    ): ResponseEntity<AbstractResponse> {
        val jwtTokens: JwtTokens = signUpUseCase.execute(request)

        return ResponseEntity.ok(TokenResponse(jwtTokens.accessToken, jwtTokens.refreshToken))
    }

    @PostMapping("/sign-in")
    fun signIn(
        @RequestBody
        @Valid
        request: SignInRequest
    ): ResponseEntity<AbstractResponse> {
        val jwtTokens: JwtTokens = signInUseCase.execute(request)

        return ResponseEntity.ok(TokenResponse(jwtTokens.accessToken, jwtTokens.refreshToken))
    }

    @PostMapping("/sign-in-by-phone")
    fun signIn(
        @RequestBody
        @Valid
        request: SignInByPhoneRequest
    ): ResponseEntity<AbstractResponse> {
        val jwtTokens: JwtTokens = signInUseCase.execute(request)

        return ResponseEntity.ok(TokenResponse(jwtTokens.accessToken, jwtTokens.refreshToken))
    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestBody
        @Valid
        request: RefreshTokenRequest
    ): ResponseEntity<AbstractResponse> {
        val jwtTokens: JwtTokens = tokenRefreshUseCase.execute(token = request.refreshToken)

        return ResponseEntity.ok(TokenResponse(jwtTokens.accessToken, jwtTokens.refreshToken))
    }

    @PostMapping("/restore-by-phone")
    fun restorePasswordByEmail(
        @RequestBody
        @Valid
        request: RestorePasswordByPhoneRequest
    ): ResponseEntity<AbstractResponse> {
        userRestorePasswordUseCase.executeWithPhone(request.phone)

        return ResponseEntity.ok(SuccessResponse())
    }

    @PostMapping("/restore-by-email")
    fun restorePasswordByEmail(
        @RequestBody
        @Valid
        request: RestorePasswordByEmailRequest
    ): ResponseEntity<AbstractResponse> {
        userRestorePasswordUseCase.executeWithEmail(request.email)

        return ResponseEntity.ok(SuccessResponse())
    }

    @PutMapping("/restore-by-email")
    fun restorePasswordChangeByEmail(
        @RequestBody
        @Valid
        request: ChangeRestorePasswordRequest
    ): ResponseEntity<AbstractResponse> {
        userRestorePasswordChangeUseCase.executeByToken(
            UserRestorePasswordChangeUseCase.Input(
                token = request.confirmationToken,
                password = request.password
            )
        )

        return ResponseEntity.ok(SuccessResponse())
    }

    @PutMapping("/restore-by-phone")
    fun restorePasswordChangeByPhone(
        @RequestBody
        @Valid
        request: ChangeRestorePasswordRequest
    ): ResponseEntity<AbstractResponse> {
        userRestorePasswordChangeUseCase.executeByPhone(
            UserRestorePasswordChangeUseCase.Input(
                token = request.confirmationToken,
                password = request.password,
                phone = request.phone
            )
        )

        return ResponseEntity.ok(SuccessResponse())
    }

    @GetMapping("/restore/{token}")
    fun getGreetingWithResponseEntity(
        @PathVariable(name = "token")
        @NotBlank
        @Size(message = "Token must be maximum ${Constants.MAX_TOKEN} characters", max = Constants.MAX_TOKEN)
        token: String
    ): ResponseEntity<String> {
        val httpHeaders: HttpHeaders = HttpHeaders().apply {
            contentType = MediaType.TEXT_HTML
        }
        val message = "Password restore <a href=\"{link}\">link</a>.".replace(
            "{link}", Constants.RESET_PASSWORD_DEEPLINK
                .replace("{token}", token)
                .replace("{appName}", "")
        )
        return ResponseEntity
            .ok()
            .headers(httpHeaders)
            .body(message)
    }
}
