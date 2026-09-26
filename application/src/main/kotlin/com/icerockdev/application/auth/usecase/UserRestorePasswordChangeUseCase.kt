package com.icerockdev.application.auth.usecase

import com.icerockdev.application.auth.enum.ConfirmationTokenType
import com.icerockdev.application.auth.service.ConfirmationTokenService
import com.icerockdev.application.user.dto.ConfirmationTokenDto
import com.icerockdev.application.user.dto.UserDto
import com.icerockdev.application.user.service.UserService
import com.icerockdev.exception.ValidationException
import com.icerockdev.i18n.I18N
import com.icerockdev.i18n.LK
import com.icerockdev.util.HashUtils
import java.time.LocalDateTime
import org.springframework.stereotype.Component

@Component
class UserRestorePasswordChangeUseCase(
    private val confirmationTokenService: ConfirmationTokenService,
    private val userService: UserService,
    private val i18N: I18N
) {

    fun executeByPhone(input: Input) {
        input.phone ?: throw ValidationException(i18N.t(LK.phoneIsRequiredForPasswordRecovery))
        execute(input)
    }

    fun executeByToken(input: Input) {
        execute(input.copy(phone = null))
    }

    private fun execute(input: Input) {
        val confirmationTokenDto: ConfirmationTokenDto =
            when (input.phone) {
                null -> confirmationTokenService.getByTokenAndType(
                    input.token,
                    ConfirmationTokenType.RESTORE_PASSWORD
                )

                else -> confirmationTokenService.getByPhone(
                    input.token,
                    input.phone,
                    ConfirmationTokenType.RESTORE_PASSWORD
                )
            } ?: throw ValidationException(i18N.t(LK.passwordRecoveryLinkExpired))

        val userDto: UserDto = userService.getById(confirmationTokenDto.entityId)

        userDto.copy(
            password = HashUtils.generatePasswordHash(input.password),
            updatedAt = LocalDateTime.now()
        ).also { userService.save(it) }

        confirmationTokenService.deleteAllByEntityIdAndEntityTypeAndType(
            entityId = userDto.id,
            entityType = userDto.role,
            type = ConfirmationTokenType.RESTORE_PASSWORD
        )
    }

    data class Input(
        val token: String,
        val password: String,
        val phone: String? = null
    )
}
