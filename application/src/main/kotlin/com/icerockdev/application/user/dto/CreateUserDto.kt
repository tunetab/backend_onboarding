package com.icerockdev.application.user.dto

import com.icerockdev.application.user.model.Status
import com.icerockdev.auth.acl.Role

data class CreateUserDto(
    val phone: String,
    val email: String,
    val password: String,
    val role: Role,
    val status: Status,
    val isTest: Boolean = false
)
