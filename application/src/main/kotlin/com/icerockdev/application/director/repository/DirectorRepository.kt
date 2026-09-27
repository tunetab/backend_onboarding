package com.icerockdev.application.director.repository

import com.icerockdev.application.director.model.Director
import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DirectorRepository : JpaRepository<Director, UUID> {
    fun findAllByOrderByNameAsc(pageable: Pageable): Page<Director>
}
