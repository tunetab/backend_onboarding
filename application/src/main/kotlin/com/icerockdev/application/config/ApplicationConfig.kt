package com.icerockdev.application.config

import com.icerockdev.application.auth.abstract.INotificationService
import com.icerockdev.application.auth.repository.TokenDataRepository
import com.icerockdev.application.auth.security.CustomUserDetailService
import com.icerockdev.auth.revoke.RevokeTokenService
import com.icerockdev.auth.user.UserKey
import java.util.UUID
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class ApplicationConfig(
    private val customUserDetailService: CustomUserDetailService,
    private val tokenDataRepository: TokenDataRepository
) {

    @Bean
    fun authenticationProvider(
        passwordEncoder: PasswordEncoder
    ): AuthenticationProvider =
        DaoAuthenticationProvider(customUserDetailService).apply {
            setPasswordEncoder(passwordEncoder)
        }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager? {
        return config.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }

    @Bean("mainRevokeTokenService")
    fun revokeTokenService(): RevokeTokenService<UserKey> {
        return RevokeTokenService(
            repository = tokenDataRepository
        )
    }

    @Bean
    fun getINotificationService(): INotificationService {
        return object : INotificationService {
            override fun sendEmail(
                subject: String,
                to: Map<String, String?>,
                emailBody: String,
                async: Boolean,
                settingsForSaveNotification: INotificationService.ApplicationSettingsForSaveNotification?
            ) {
                TODO("Not yet implemented")
            }

            override fun sendPush(
                userIds: List<UUID>,
                notification: INotificationService.ApplicationNotificationModuleData,
                data: Map<String, String>?,
                settingsForSaveNotification: INotificationService.ApplicationSettingsForSaveNotification?
            ) {
                TODO("Not yet implemented")
            }

            override fun sendSms(
                message: String,
                phone: String,
                settingsForSaveNotification: INotificationService.ApplicationSettingsForSaveNotification?
            ) {
                TODO("Not yet implemented")
            }
        }
    }
}
