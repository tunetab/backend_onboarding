package com.icerockdev.application.auth.repository

import com.icerockdev.application.auth.model.RevokeToken
import com.icerockdev.auth.acl.Role
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RevokeTokenRepository : JpaRepository<RevokeToken, UUID> {
    @Query("DELETE FROM RevokeToken WHERE expiredAt < :expiredAt")
    @Modifying
    fun deleteAllExpired(@Param("expiredAt") expiredAt: LocalDateTime): Int

    @Query("SELECT rt FROM RevokeToken rt WHERE rt.expiredAt >= CURRENT_TIMESTAMP")
    fun getAllNotExpired(): List<RevokeToken>

    fun findByEntityIdAndEntityType(entityId: UUID, entityType: Role): RevokeToken?
}
