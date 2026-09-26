package com.icerockdev.application.tests.fixture

import com.icerockdev.application.user.model.Status
import com.icerockdev.application.user.model.User
import com.icerockdev.application.user.repository.UserRepository
import com.icerockdev.auth.acl.Role
import com.icerockdev.util.HashUtils
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestComponent

@TestComponent
class UserFixtureBuilder {

    @Autowired
    private lateinit var userRepository: UserRepository

    fun buildUser(
        email: String,
        phone: String,
        password: String,
        isTest: Boolean = false,
        role: Role = Role.USER
    ): User {
        return userRepository.save(
            User(
                id = UUID.randomUUID(),
                email = email,
                phone = phone,
                password = HashUtils.generatePasswordHash(password),
                role = role,
                status = Status.ACTIVE,
                isTest = isTest,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )
    }
}
