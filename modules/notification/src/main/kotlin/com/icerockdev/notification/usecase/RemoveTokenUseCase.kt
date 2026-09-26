package com.icerockdev.notification.usecase

import com.icerockdev.notification.service.PushTokenService
import org.springframework.stereotype.Component

/**
 * Delete FCM token
 */
@Component
internal class RemoveTokenUseCase(
    private val pushTokenService: PushTokenService
) {
    fun execute(token: String) {
        return pushTokenService.delete(token)
    }
}
