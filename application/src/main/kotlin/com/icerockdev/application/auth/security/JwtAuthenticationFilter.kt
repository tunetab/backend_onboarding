package com.icerockdev.application.auth.security

import com.icerockdev.application.auth.jwt.CustomJWTTokenGenerator
import com.icerockdev.application.user.dto.UserDto
import com.icerockdev.application.user.service.UserService
import com.icerockdev.auth.revoke.IRevokeTokenService
import com.icerockdev.auth.user.CustomUserDetails
import com.icerockdev.auth.user.UserKey
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtAuthenticationFilter(
    private val userService: UserService,
    private val jwtTokenGenerator: CustomJWTTokenGenerator,
    private val revokeTokenService: IRevokeTokenService<UserKey>
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader: String? = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return filterChain.doFilter(request, response)
        }
        try {
            @Suppress("MagicNumber")
            val jwt: String = authHeader.substring(7)
            val userKey = jwtTokenGenerator.getUserKey<UserKey>(jwt)
            if (!revokeTokenService.checkIsActive(userKey, jwtTokenGenerator.getIssuedAt(jwt))) {
                return filterChain.doFilter(request, response)
            }

            val userDto: UserDto = userService.getById(userKey.id)

            if (SecurityContextHolder.getContext().authentication == null) {
                val userDetails = CustomUserDetails(
                    id = userDto.id,
                    username = userDto.getName(),
                    password = userDto.password,
                    role = userDto.role,
                    isActive = userDto.isActive()
                )
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        } catch (e: JwtException) {
            logger.warn(e.localizedMessage, e)
        }

        filterChain.doFilter(request, response)
    }
}
