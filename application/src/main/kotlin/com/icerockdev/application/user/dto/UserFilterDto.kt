package com.icerockdev.application.user.dto

import com.icerockdev.application.user.model.Status
import com.icerockdev.auth.acl.Role
import java.util.UUID

data class UserFilterDto(
    val id: UUID? = null,
    val phone: String? = null,
    val email: String? = null,
    val status: Status? = null,
    val role: Role? = null,
    val isTest: Boolean? = null
)
