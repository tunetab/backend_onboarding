package com.icerockdev.application.director.usecase

import com.icerockdev.application.director.dto.DirectorDto
import com.icerockdev.application.director.service.DirectorService
import java.time.LocalDateTime
import org.springframework.stereotype.Component

@Component
class CreateDirectorUseCaseImpl(
    private val directorService: DirectorService,
) : CreateDirectorUseCase {
    override fun invoke(input: CreateDirectorUseCase.Input): DirectorDto {
        val now = LocalDateTime.now()
        return directorService.save(
            DirectorDto(
                name = input.request.name,
                country = input.request.country,
                winOscar = requireNotNull(input.request.winOscar),
                oscarWinnerTitle = input.request.oscarWinnerTitle,
                createdAt = now,
                updatedAt = now,
            )
        )
    }
}
