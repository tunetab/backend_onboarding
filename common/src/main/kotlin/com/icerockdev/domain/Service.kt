package com.icerockdev.domain

import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface Service<DTO, FILTER> {
    fun getPage(pagination: Pageable, filterDto: FILTER? = null): Page<DTO>

    fun getById(id: UUID): DTO

    fun findById(id: UUID): DTO?

    fun save(dto: DTO): DTO

    fun delete(dto: DTO): Boolean

    fun delete(id: UUID): Boolean
}
