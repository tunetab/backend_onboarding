package com.icerockdev.application.auth.repository

import com.icerockdev.application.auth.model.RefreshToken
import com.icerockdev.auth.acl.Role
import java.time.LocalDateTime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import org.springframework.transaction.annotation.Transactional

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, UUID> {
    fun findByTokenAndEntityType(token: String, entityType: Role): RefreshToken?

    fun deleteAllByEntityIdAndEntityTypeAndExpiredAtIsLessThanEqual(
        entityId: UUID,
        entityType: Role,
        date: LocalDateTime
    )

    @Transactional
    fun deleteAllByEntityIdAndEntityType(
        entityId: UUID,
        entityType: Role
    )
}
