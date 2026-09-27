package com.icerockdev.application.director.usecase

import com.icerockdev.application.director.dto.DirectorDto
import com.icerockdev.domain.UseCase
import org.springframework.data.domain.Pageable

interface GetDirectorListUseCase : UseCase<GetDirectorListUseCase.Input, GetDirectorListUseCase.Output> {
    data class Input(val pagination: Pageable)

    data class Output(
        val directors: List<DirectorDto>,
        val totalElements: Int,
        val totalPages: Int,
    )
}
