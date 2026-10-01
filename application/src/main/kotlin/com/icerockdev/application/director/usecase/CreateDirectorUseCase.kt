package com.icerockdev.application.director.usecase

import com.icerockdev.application.director.dto.DirectorDto
import com.icerockdev.application.director.request.CreateDirectorRequest
import com.icerockdev.domain.UseCase

interface CreateDirectorUseCase : UseCase<CreateDirectorUseCase.Input, DirectorDto> {
    data class Input(val request: CreateDirectorRequest)
}
