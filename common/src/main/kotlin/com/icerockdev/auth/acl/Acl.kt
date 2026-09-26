package com.icerockdev.auth.acl

const val ROLE_CLAIM = "role"
const val TYPE_CLAIM = "type"
const val USER_CLAIM = "user"

const val CLAIM_SUBJECT = "Authentication"

enum class Role {
    SUPERADMIN,
    ADMIN,
    USER;

    fun isAdmin(): Boolean {
        return this in listOf(ADMIN, SUPERADMIN)
    }
}
