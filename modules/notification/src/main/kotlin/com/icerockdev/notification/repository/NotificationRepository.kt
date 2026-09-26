package com.icerockdev.notification.repository

import com.icerockdev.auth.acl.Role
import com.icerockdev.notification.model.Notification
import java.util.UUID
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
internal interface NotificationRepository : JpaRepository<Notification, UUID> {
    fun findAllByEntityIdAndEntityType(entityId: UUID, entityType: Role, pagination: Pageable): List<Notification>

    @Transactional
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.entityId = :entityId AND n.entityType = :entityType")
    fun setAllIsReadByEntityIdAndEntityType(
        @Param("entityId")
        entityId: UUID,
        @Param("entityType")
        entityType: Role
    )

    @Transactional
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.entityId = :entityId AND n.entityType = :entityType AND n.type = :type")
    fun setAllIsReadByEntityIdAndEntityTypeAndType(
        @Param("entityId")
        entityId: UUID,
        @Param("entityType")
        entityType: Role,
        @Param("type")
        type: Int
    )

    @Transactional
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.entityId = :entityId AND n.entityType = :entityType AND n.id = :id")
    fun setIsReadByIdAndEntityIdAndEntityType(
        @Param("entityId")
        entityId: UUID,
        @Param("entityType")
        entityType: Role,
        @Param("id")
        id: UUID
    )
}
