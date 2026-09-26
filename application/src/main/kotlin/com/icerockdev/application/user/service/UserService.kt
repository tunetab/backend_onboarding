package com.icerockdev.application.user.service

import com.icerockdev.application.user.dto.UserDto
import com.icerockdev.application.user.dto.UserFilterDto
import com.icerockdev.application.user.repository.UserRepository
import com.icerockdev.application.user.repository.UserSpecification
import com.icerockdev.exception.NotFoundException
import com.icerockdev.i18n.I18N
import com.icerockdev.i18n.LK
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val i18N: I18N
) : com.icerockdev.domain.Service<UserDto, UserFilterDto> {

    override fun getPage(
        pagination: Pageable,
        filterDto: UserFilterDto?
    ): Page<UserDto> {
        return userRepository.findAll(
                UserSpecification.create(filter = filterDto),
                pagination
            )
            .map { it.toDto() }
    }

    override fun getById(id: UUID): UserDto {
        return findById(id) ?: throw NotFoundException(i18N.t(LK.userNotFound))
    }

    override fun findById(id: UUID): UserDto? {
        return userRepository.findByIdOrNull(id)?.toDto()
    }

    override fun save(dto: UserDto): UserDto {
        return userRepository
            .save(dto.toEntity())
            .toDto()
    }

    override fun delete(dto: UserDto): Boolean {
        return delete(dto.id)
    }

    override fun delete(id: UUID): Boolean {
        save(getById(id).copy(deletedAt = LocalDateTime.now()))
        return true
    }

    fun getByEmail(email: String): UserDto {
        return userRepository
            .findByEmail(email)
            ?.toDto()
            ?: throw NotFoundException(i18N.t(LK.userNotFound))
    }

    fun getByPhone(phone: String): UserDto {
        return userRepository
            .findByPhone(phone)
            ?.toDto()
            ?: throw NotFoundException(i18N.t(LK.userNotFound))
    }

    fun existByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    fun existByPhone(phone: String): Boolean {
        return userRepository.existsByPhone(phone)
    }
}
