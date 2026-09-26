package com.icerockdev.notification.usecase

import com.icerockdev.notification.dto.PushTokenDto
import com.icerockdev.notification.service.PushTokenService
import java.util.UUID
import org.springframework.stereotype.Component

/**
 * Adds FCM token
 */
@Component
internal class AddTokenUseCase(
    private val pushTokenService: PushTokenService
) {
    fun execute(entityId: UUID, token: String) {
        val pushTokenDto = PushTokenDto(token = token, entityId = entityId)
        pushTokenService.insert(pushTokenDto)
    }
}
