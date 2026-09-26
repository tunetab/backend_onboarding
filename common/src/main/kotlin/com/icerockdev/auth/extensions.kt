package com.icerockdev.auth

import com.icerockdev.auth.user.CustomUserDetails
import com.icerockdev.auth.user.UserKey
import com.icerockdev.exception.NotFoundException
import org.springframework.security.core.Authentication

fun Authentication.getUserKey(): UserKey {
    return this.principal?.let {
        val user = it as CustomUserDetails
        UserKey(
            user.id,
            user.role
        )
    } ?: throw NotFoundException()
}
