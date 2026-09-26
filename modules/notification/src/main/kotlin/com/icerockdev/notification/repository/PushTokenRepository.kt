package com.icerockdev.notification.repository

import com.icerockdev.notification.model.PushToken
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
internal interface PushTokenRepository : JpaRepository<PushToken, UUID> {
    fun findByEntityIdIn(entityIds: List<UUID>): List<PushToken>
    fun deleteByToken(token: String)

    @Transactional
    fun deleteAllByTokenIn(tokens: List<String>): Int
}
