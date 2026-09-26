package com.icerockdev.application.user.repository

import com.icerockdev.application.user.model.User
import com.icerockdev.domain.JpaWithSpecificationRepository
import java.util.UUID
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaWithSpecificationRepository<User, UUID> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    fun existsByPhone(phone: String): Boolean
    fun findByPhone(phone: String): User?
}
