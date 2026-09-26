package com.icerockdev.auth.user

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.icerockdev.auth.acl.Role
import java.util.UUID

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserKey(
    val id: UUID,
    val role: Role
)
