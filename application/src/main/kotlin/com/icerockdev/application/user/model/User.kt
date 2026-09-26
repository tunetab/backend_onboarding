package com.icerockdev.application.user.model

import com.icerockdev.application.common.Constants
import com.icerockdev.application.user.dto.UserDto
import com.icerockdev.auth.acl.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "users")
@SQLRestriction("deleted_at IS NULL") // it applied to all queries
class User(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    val id: UUID,

    @Column(name = "phone", nullable = false, unique = true, length = Constants.MAX_PHONE)
    val phone: String,

    @Column(name = "email", nullable = false, unique = true, length = Constants.MAX_EMAIL)
    val email: String,

    @Column(name = "password", nullable = false, length = Constants.MAX_PASSWORD)
    val password: String,

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    val role: Role,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: Status,

    @Column(name = "is_test", nullable = false)
    val isTest: Boolean,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime,

    @Column(name = "deleted_at", nullable = true)
    val deletedAt: LocalDateTime? = null
) {
    fun toDto() = UserDto(
        id = id,
        phone = phone,
        email = email,
        password = password,
        isTest = isTest,
        status = status,
        role = role,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt
    )
}
