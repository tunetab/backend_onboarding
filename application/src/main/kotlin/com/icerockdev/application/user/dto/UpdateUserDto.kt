package com.icerockdev.application.user.dto

import com.icerockdev.application.user.model.Status
import com.icerockdev.auth.acl.Role
import java.time.LocalDateTime

class UpdateUserDto(
    val email: String?,
    val phone: String?,
    val password: String?,
    val role: Role?,
    val status: Status?,
    val isTest: Boolean? = false,
    val updatedAt: LocalDateTime
)
