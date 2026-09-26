package com.icerockdev.application.user.response

import com.icerockdev.application.user.dto.UserDto
import com.icerockdev.application.user.model.Status
import com.icerockdev.auth.acl.Role
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val email: String,
    val phone: String,
    val status: Status,
    val role: Role,
    val isTest: Boolean,
    val createdAt: String,
    val updatedAt: String
) {
    constructor(dto: UserDto) : this(
        id = dto.id,
        email = dto.email,
        phone = dto.phone,
        status = dto.status,
        role = dto.role,
        isTest = dto.isTest,
        createdAt = dto.createdAt.toString(),
        updatedAt = dto.updatedAt.toString()
    )
}
