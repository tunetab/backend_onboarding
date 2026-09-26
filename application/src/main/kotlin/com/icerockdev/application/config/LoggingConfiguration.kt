package com.icerockdev.application.config

import com.icerockdev.log.RequestAndResponseLoggingFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LoggingConfiguration {

    @Bean
    fun requestResponseLoggingFilter(): RequestAndResponseLoggingFilter {
        return RequestAndResponseLoggingFilter {
            sensitiveFields = listOf(
                "password",
                "passwordRepeat",
                "accessToken",
                "refreshToken",
            )
        }
    }
}
