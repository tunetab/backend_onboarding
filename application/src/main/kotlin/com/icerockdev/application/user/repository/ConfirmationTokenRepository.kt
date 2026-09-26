package com.icerockdev.application.user.repository

import com.icerockdev.application.auth.enum.ConfirmationTokenType
import com.icerockdev.application.user.model.ConfirmationToken
import com.icerockdev.auth.acl.Role
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

@Transactional
interface ConfirmationTokenRepository : CrudRepository<ConfirmationToken, UUID> {

    fun existsByTokenAndType(token: String, type: ConfirmationTokenType): Boolean
    fun findByTokenAndTypeAndExpiredAtIsGreaterThanEqual(token: String, type: ConfirmationTokenType, date: LocalDateTime): ConfirmationToken?

    fun findByTokenAndPhoneAndTypeAndExpiredAtIsGreaterThanEqual(
        token: String,
        phone: String,
        type: ConfirmationTokenType,
        date: LocalDateTime
    ): ConfirmationToken?

    fun deleteAllByEntityIdAndEntityTypeAndType(userId: UUID, entityType: Role, type: ConfirmationTokenType)

    fun findByEntityIdAndEntityTypeAndPhoneAndType(
        entityId: UUID,
        entityType: Role,
        phone: String?,
        type: ConfirmationTokenType
    ): ConfirmationToken?
}
