package com.icerockdev.notification.service

import com.icerockdev.notification.dto.PushTokenDto
import com.icerockdev.notification.model.PushToken
import com.icerockdev.notification.repository.PushTokenRepository
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class PushTokenService(
    private val pushTokenRepository: PushTokenRepository
) {

    fun insert(pushTokenDto: PushTokenDto): UUID {
        return pushTokenRepository.save(PushToken(pushTokenDto)).id
    }

    fun getTokensByUserIds(entityIds: List<UUID>): List<PushToken> {
        return pushTokenRepository.findByEntityIdIn(entityIds)
    }

    fun delete(pushTokenDto: PushTokenDto) {
        return pushTokenRepository.delete(PushToken(pushTokenDto))
    }

    @Transactional
    fun delete(token: String) {
        return pushTokenRepository.deleteByToken(token)
    }

    fun deleteByTokenList(tokenList: List<String>): Int {
        return pushTokenRepository.deleteAllByTokenIn(tokenList)
    }
}
