package com.icerockdev.notification.config

import com.icerockdev.notification.NotificationService
import com.icerockdev.notification.repository.PushTokenRepository
import com.icerockdev.service.email.MailerService
import com.icerockdev.service.email.SMTPConfig
import com.icerockdev.service.email.SMTPSecure
import com.icerockdev.service.email.fromAlias
import com.icerockdev.service.fcmpush.FCMConfig
import com.icerockdev.service.fcmpush.IPushRepository
import com.icerockdev.service.fcmpush.PushService
import com.icerockdev.service.sms.SmsService
import com.icerockdev.service.sms.driver.twilio.TwilioConfig
import com.icerockdev.service.sms.driver.twilio.TwilioDriver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NotificationConfig(
    private val smtpSettings: SMTPSettings,
    private val twilioSettings: TwilioSettings,
    private val fcmSettings: FcmSettings
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @Autowired
    private lateinit var pushTokenRepository: PushTokenRepository

    @Bean
    fun getNotificationService(): NotificationService {
        return NotificationService(smtpSettings)
    }

    @Bean
    fun getMailerService(): MailerService = MailerService(
        coroutineScope = this.coroutineScope,
        config = SMTPConfig(
            host = smtpSettings.host,
            port = smtpSettings.port,
            smtpAuth = smtpSettings.auth,
            smtpSecure = SMTPSecure.fromAlias(smtpSettings.secure.lowercase()),
            username = smtpSettings.user,
            password = smtpSettings.password
        )
    )

    @Bean
    fun getSmsService(): SmsService = SmsService(
        coroutineScope = coroutineScope,
        driver = TwilioDriver(
            config = TwilioConfig(
                accountSID = twilioSettings.accountSid,
                authToken = twilioSettings.authToken,
                phone = twilioSettings.phone
            )
        )
    )

    @Bean
    fun getPushService(): PushService = PushService(
        coroutineScope = coroutineScope,
        pushRepository = object : IPushRepository {
            override fun deleteByTokenList(tokenList: List<String>): Int {
                return pushTokenRepository.deleteAllByTokenIn(tokenList)
            }
        },
        config = FCMConfig(
            googleServiceAccountJson = fcmSettings.googleServiceAccountJson,
            lateInit = true
        )
    )
}
