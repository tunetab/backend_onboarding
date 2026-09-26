package com.icerockdev.application.user.service

import com.icerockdev.application.user.dto.UserProfileDto
import com.icerockdev.application.user.repository.UserProfileRepository
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
class UserProfileService(
    private val userProfileRepository: UserProfileRepository,
    private val i18N: I18N
) : com.icerockdev.domain.Service<UserProfileDto, Unit> {
    override fun getPage(pagination: Pageable, filterDto: Unit?): Page<UserProfileDto> {
        TODO("Not yet implemented")
    }

    override fun getById(id: UUID): UserProfileDto {
        return findById(id) ?: throw NotFoundException(i18N.t(LK.userProfileNotFound))
    }

    override fun findById(id: UUID): UserProfileDto? {
        return userProfileRepository.findByIdOrNull(id)?.toDto()
    }

    override fun delete(dto: UserProfileDto): Boolean {
        return delete(dto.userId)
    }

    override fun save(dto: UserProfileDto): UserProfileDto {
        return userProfileRepository.save(dto.toEntity()).toDto()
    }

    override fun delete(id: UUID): Boolean {
        userProfileRepository.findByIdOrNull(id)?.toDto()?.let {
            save(it.copy(deletedAt = LocalDateTime.now()))
        }

        return true
    }
}
