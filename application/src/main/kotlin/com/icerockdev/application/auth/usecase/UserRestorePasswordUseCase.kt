package com.icerockdev.application.auth.usecase

import com.icerockdev.application.auth.abstract.INotificationService
import com.icerockdev.application.auth.enum.ConfirmationTokenType
import com.icerockdev.application.auth.service.ConfirmationTokenService
import com.icerockdev.application.common.Constants
import com.icerockdev.application.config.ConfigurationEnvironment
import com.icerockdev.application.user.dto.ConfirmationTokenDto
import com.icerockdev.application.user.dto.UserDto
import com.icerockdev.application.user.service.UserService
import com.icerockdev.exception.BadRequestException
import com.icerockdev.exception.ServerErrorException
import com.icerockdev.i18n.I18N
import com.icerockdev.i18n.LK
import com.icerockdev.util.HashUtils
import java.time.LocalDateTime
import org.springframework.stereotype.Component

@Component
class UserRestorePasswordUseCase(
    private val iNotificationService: INotificationService,
    private val userService: UserService,
    private val confirmationTokenService: ConfirmationTokenService,
    private val env: ConfigurationEnvironment,
    private val i18N: I18N
) {
    fun executeWithEmail(email: String) {
        val userDto: UserDto = userService.getByEmail(email)

        checkExistsNotExpiredTokenOrCode(userDto, needToken = true)

        val confirmationToken: String = getConfirmationTokenOrCode(userDto, needToken = true)
        val link = if (userDto.role.isAdmin()) {
            env.getAdminConfirmationTokenUrl(confirmationToken)
        } else {
            env.getConfirmationTokenUrl(confirmationToken)
        }
        val emailBody: String = i18N.t(LK.emailPasswordRestoreMessageBody)
            .replace("{link}", link)

        iNotificationService.sendEmail(
            subject = i18N.t(LK.emailPasswordRestoreMessageTitle),
            to = mutableMapOf(email to email),
            emailBody = emailBody,
            async = true
        )
    }

    fun executeWithPhone(phone: String) {
        val userDto: UserDto = userService.getByPhone(phone)

        checkExistsNotExpiredTokenOrCode(userDto, needToken = false)

        val confirmationCode: String = getConfirmationTokenOrCode(userDto = userDto, needToken = false)

        iNotificationService.sendSms(
            message = confirmationCode,
            phone = phone
        )
    }

    private fun checkExistsNotExpiredTokenOrCode(userDto: UserDto, needToken: Boolean) {
        val model = confirmationTokenService.getByEntityIdAndEntityTypeAndPhoneAndType(
            entityId = userDto.id,
            entityType = userDto.role,
            phone = if (needToken) null else userDto.phone,
            type = ConfirmationTokenType.RESTORE_PASSWORD
        ) ?: return

        if (model.expiredAt > LocalDateTime.now()) {
            throw BadRequestException(i18N.t(LK.passwordRestoreRequestAlreadyExist))
        }
    }

    private fun getConfirmationTokenOrCode(userDto: UserDto, needToken: Boolean): String {
        val now: LocalDateTime = LocalDateTime.now()

        confirmationTokenService.deleteAllByEntityIdAndEntityTypeAndType(
            entityId = userDto.id,
            entityType = userDto.role,
            type = ConfirmationTokenType.RESTORE_PASSWORD
        )

        repeat(Constants.UNIQUE_TOKEN_GENERATE_MAX_ATTEMPTS) {
            val token = if (needToken) {
                HashUtils.generateRandomToken()
            } else {
                HashUtils.generateRandomCode(Constants.CODE_SIZE)
            }

            if (confirmationTokenService.existsByTokenAndType(token, ConfirmationTokenType.RESTORE_PASSWORD)) {
                return@repeat
            }

            val confirmationTokenDto = ConfirmationTokenDto(
                type = ConfirmationTokenType.RESTORE_PASSWORD,
                token = token,
                entityId = userDto.id,
                entityType = userDto.role,
                expiredAt = now.plusMinutes(
                    (if (needToken) {
                        Constants.TOKEN_EXPIRED_MINUTES
                    } else {
                        Constants.CODE_EXPIRED_MINUTES
                    }).toLong()
                ),
                phone = if (needToken) null else userDto.phone
            )

            return confirmationTokenService.save(confirmationTokenDto).token
        }

        throw ServerErrorException()
    }
}
