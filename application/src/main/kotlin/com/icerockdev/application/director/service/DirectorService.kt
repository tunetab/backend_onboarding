package com.icerockdev.application.director.service

import com.icerockdev.application.director.dto.DirectorDto
import com.icerockdev.application.director.repository.DirectorRepository
import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class DirectorService(
    private val directorRepository: DirectorRepository,
) : com.icerockdev.domain.Service<DirectorDto, Unit> {
    override fun getPage(pagination: Pageable, filterDto: Unit?): Page<DirectorDto> =
        directorRepository.findAllByOrderByNameAsc(pagination).map { it.toDto() }

    override fun getById(id: UUID): DirectorDto =
        findById(id) ?: throw NoSuchElementException("Director with id $id was not found")

    override fun findById(id: UUID): DirectorDto? =
        directorRepository.findByIdOrNull(id)?.toDto()

    override fun save(dto: DirectorDto): DirectorDto =
        directorRepository.save(dto.toEntity()).toDto()

    override fun delete(dto: DirectorDto): Boolean = delete(dto.id)

    override fun delete(id: UUID): Boolean {
        directorRepository.deleteById(id)
        return true
    }
}
