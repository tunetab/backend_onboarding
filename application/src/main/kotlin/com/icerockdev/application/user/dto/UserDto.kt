package com.icerockdev.application.user.dto

import com.icerockdev.application.user.model.Status
import com.icerockdev.application.user.model.User
import com.icerockdev.auth.acl.Role
import java.time.LocalDateTime
import java.util.UUID

data class UserDto(
    val id: UUID = UUID.randomUUID(),
    val phone: String,
    val email: String,
    val password: String,
    val role: Role,
    val status: Status,
    val isTest: Boolean = false,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime? = null
) {
    fun isActive(): Boolean {
        return status == Status.ACTIVE
    }

    fun getName(): String {
        return email
    }

    fun toEntity() = User(
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
