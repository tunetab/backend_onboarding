package com.icerockdev.notification.tests.config

import com.icerockdev.notification.abstract.INotificationTypeListByUserType
import com.icerockdev.notification.tests.fixture.TestNotificationTypeListByUserType
import com.icerockdev.service.email.MailerService
import com.icerockdev.service.fcmpush.PushService
import com.icerockdev.service.sms.SmsService
import io.mockk.every
import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class NotificationTestConfiguration {

    @Bean
    @Primary
    fun getTestMailerService(): MailerService {
        val service = mockk<MailerService>()
        every { service.compose() } returns mockk(relaxed = true)

        return service
    }

    @Bean
    @Primary
    fun getTestPushService(): PushService {
        return mockk(relaxed = true)
    }

    @Bean
    @Primary
    fun getTestSmsService(): SmsService {
        return mockk(relaxed = true)
    }

    @Bean
    @Primary
    fun getTestINotificationTypeListByUserType(): INotificationTypeListByUserType {
        return TestNotificationTypeListByUserType()
    }
}
