package com.icerockdev.application.auth.security

import com.icerockdev.application.user.dto.UserDto
import com.icerockdev.application.user.service.UserService
import com.icerockdev.auth.user.CustomUserDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(
    private val userService: UserService,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val userDto: UserDto = userService.getByEmail(username)
        return CustomUserDetails(
            id = userDto.id,
            username = userDto.getName(),
            password = userDto.password,
            role = userDto.role,
            isActive = userDto.isActive()
        )
    }
}
