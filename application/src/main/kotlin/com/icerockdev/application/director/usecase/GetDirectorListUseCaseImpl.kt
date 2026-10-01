package com.icerockdev.application.director.usecase

import com.icerockdev.application.director.service.DirectorService
import org.springframework.stereotype.Component

@Component
class GetDirectorListUseCaseImpl(
    private val directorService: DirectorService,
) : GetDirectorListUseCase {
    override fun invoke(input: GetDirectorListUseCase.Input): GetDirectorListUseCase.Output {
        val page = directorService.getPage(input.pagination)
        return GetDirectorListUseCase.Output(
            directors = page.toList(),
            totalElements = page.totalElements.toInt(),
            totalPages = page.totalPages,
        )
    }
}
