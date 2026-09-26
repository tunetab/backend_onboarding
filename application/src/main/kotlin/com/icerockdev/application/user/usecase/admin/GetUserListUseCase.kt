package com.icerockdev.application.user.usecase.admin

import com.icerockdev.application.user.dto.UserDto
import com.icerockdev.application.user.dto.UserFilterDto
import com.icerockdev.auth.user.UserKey
import com.icerockdev.domain.UseCase
import org.springframework.data.domain.Pageable

interface GetUserListUseCase : UseCase<GetUserListUseCase.Input, GetUserListUseCase.Output> {

    data class Input(
        val pagination: Pageable,
        val filterDto: UserFilterDto,
        val credentials: UserKey
    )

    data class Output(
        val users: List<UserDto>,
        val totalElements: Int,
        val totalPages: Int,
    )
}
