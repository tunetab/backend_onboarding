package com.icerockdev.application.user.model

import com.icerockdev.application.user.dto.UserProfileDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "user_profiles")
@SQLRestriction("deleted_at IS NULL") // it applied to all queries
class UserProfile(
    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    val userId: UUID,

    @Column(name = "firstname", nullable = false)
    val firstName: String,

    @Column(name = "lastname", nullable = true)
    val lastName: String? = null,

    @Column(name = "middlename", nullable = true)
    val middleName: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime,

    @Column(name = "deleted_at", nullable = true)
    val deletedAt: LocalDateTime? = null
) {
    fun toDto() = UserProfileDto(
        userId = userId,
        firstName = firstName,
        lastName = lastName,
        middleName = middleName,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt
    )
}
