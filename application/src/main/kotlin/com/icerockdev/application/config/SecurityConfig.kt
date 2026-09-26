package com.icerockdev.application.config

import com.icerockdev.application.auth.security.JwtAuthenticationFilter
import com.icerockdev.application.auth.security.RestAuthenticationEntryPoint
import com.icerockdev.auth.acl.Role
import com.icerockdev.encoding.EncodingFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.SecurityContextHolderFilter
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Autowired
    private lateinit var jwtAuthFilter: JwtAuthenticationFilter

    @Autowired
    private lateinit var authenticationProvider: AuthenticationProvider

    @Autowired
    private lateinit var restAuthenticationEntryPoint: RestAuthenticationEntryPoint

    @Autowired
    private lateinit var corsConfig: CorsConfig

    @Component
    data class CorsConfig(
        @Value("\${spring.web.cors.allowed-origins}")
        val allowedOrigins: List<String>,

        @Value("\${spring.web.cors.allowed-methods}")
        val allowedMethods: List<String>,

        @Value("\${spring.web.cors.max-age}")
        val maxAge: Long,

        @Value("\${spring.web.cors.allowed-headers}")
        val allowedHeaders: List<String>,

        @Value("\${spring.web.cors.exposed-headers}")
        val exposedHeaders: List<String>,
    )

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain? {
        http.csrf {
            it.disable()
        }.authorizeHttpRequests {
            it.requestMatchers("/v1/auth/logout").authenticated()
            it.requestMatchers("/v1/auth/**", "/swagger-ui/**").permitAll()
            it.requestMatchers("/admin/**").hasAnyAuthority(Role.ADMIN.name)
            it.anyRequest().authenticated()
        }.sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }.authenticationManager(ProviderManager(authenticationProvider))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(EncodingFilter(), SecurityContextHolderFilter::class.java)
            .exceptionHandling {
                it.authenticationEntryPoint(restAuthenticationEntryPoint)
            }.cors {
                it.configurationSource(
                    UrlBasedCorsConfigurationSource().apply {
                        registerCorsConfiguration("/**", CorsConfiguration().apply {
                            maxAge = corsConfig.maxAge
                            allowedHeaders = corsConfig.allowedHeaders
                            exposedHeaders = corsConfig.exposedHeaders
                            allowedOrigins = corsConfig.allowedOrigins
                            allowedMethods = corsConfig.allowedMethods
                        })
                    }
                )
            }

        return http.build()
    }
}
