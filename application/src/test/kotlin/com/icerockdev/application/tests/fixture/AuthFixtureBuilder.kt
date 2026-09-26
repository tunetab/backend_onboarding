package com.icerockdev.application.tests.fixture

import com.icerockdev.application.auth.enum.ConfirmationTokenType
import com.icerockdev.application.auth.request.SignInRequest
import com.icerockdev.application.auth.service.ConfirmationTokenService
import com.icerockdev.application.auth.usecase.SignInUseCase
import com.icerockdev.application.user.model.ConfirmationToken
import com.icerockdev.application.user.model.User
import com.icerockdev.auth.jwt.JwtTokens
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestComponent

@TestComponent
class AuthFixtureBuilder {

    @Autowired
    private lateinit var signInUseCase: SignInUseCase

    @Autowired
    private lateinit var confirmationTokenService: ConfirmationTokenService

    fun authorizeUser(email: String, password: String): JwtTokens {
        return signInUseCase.execute(SignInRequest(email, password))
    }

    fun getConfirmationToken(user: User): ConfirmationToken? {
        return confirmationTokenService.getByEntityIdAndEntityTypeAndPhoneAndType(
            entityId = user.id,
            entityType = user.role,
            phone = null,
            type = ConfirmationTokenType.RESTORE_PASSWORD
        )
    }

    fun getConfirmationCode(user: User): ConfirmationToken? {
        return confirmationTokenService.getByEntityIdAndEntityTypeAndPhoneAndType(
            entityId = user.id,
            entityType = user.role,
            phone = user.phone,
            type = ConfirmationTokenType.RESTORE_PASSWORD
        )
    }
}
