package com.icerockdev.application.user.usecase.admin.impl

import com.icerockdev.application.user.dto.UserDto
import com.icerockdev.application.user.service.UserService
import com.icerockdev.application.user.usecase.admin.GetUserListUseCase
import com.icerockdev.auth.acl.Role
import com.icerockdev.exception.ForbiddenException
import com.icerockdev.i18n.I18N
import com.icerockdev.i18n.LK
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component

@Component
class GetUserListUseCaseImpl(
    private val userService: UserService,
    private val i18N: I18N
) : GetUserListUseCase {

    override fun invoke(input: GetUserListUseCase.Input): GetUserListUseCase.Output {
        if (input.credentials.role !in listOf(Role.ADMIN, Role.SUPERADMIN)) {
            throw ForbiddenException(i18N.t(LK.actionNotAllowed))
        }

        val userPage: Page<UserDto> = userService.getPage(pagination = input.pagination, filterDto = input.filterDto)

        return GetUserListUseCase.Output(
            users = userPage.toList(),
            totalElements = userPage.totalElements.toInt(),
            totalPages = userPage.totalPages,
        )
    }
}
