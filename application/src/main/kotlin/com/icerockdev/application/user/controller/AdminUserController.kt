package com.icerockdev.application.user.controller

import com.icerockdev.api.response.EmptyResponse
import com.icerockdev.api.response.Response
import com.icerockdev.api.response.ResponseList
import com.icerockdev.application.user.dto.UserDto
import com.icerockdev.application.user.model.Status
import com.icerockdev.application.user.request.CreateUserRequest
import com.icerockdev.application.user.request.UpdateUserRequest
import com.icerockdev.application.user.request.UserFilterQuery
import com.icerockdev.application.user.response.UserResponse
import com.icerockdev.application.user.usecase.admin.ChangeUserStatusUseCase
import com.icerockdev.application.user.usecase.admin.CreateUserUseCase
import com.icerockdev.application.user.usecase.admin.DeleteUserUseCase
import com.icerockdev.application.user.usecase.admin.GetUserListUseCase
import com.icerockdev.application.user.usecase.admin.GetUserUseCase
import com.icerockdev.application.user.usecase.admin.UpdateUserUseCase
import com.icerockdev.auth.getUserKey
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import java.util.UUID
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/v1/admin/user")
class AdminUserController(
    private val createUserUseCase: CreateUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getUserListUseCase: GetUserListUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val changeUserStatusUseCase: ChangeUserStatusUseCase
) {

    @PostMapping("")
    fun createUser(
        @RequestBody
        @Valid
        request: CreateUserRequest,
        authentication: Authentication
    ): ResponseEntity<*> {
        val userId: UUID = createUserUseCase.invoke(
            CreateUserUseCase.Input(
                createDto = request.getDto(),
                credentials = authentication.getUserKey()
            )
        )

        val userDto: UserDto = getUserUseCase.invoke(
            GetUserUseCase.Input(
                id = userId,
                credentials = authentication.getUserKey()
            )
        )

        return ResponseEntity.ok(Response(data = UserResponse(userDto)))
    }

    @GetMapping("")
    fun list(
        @Valid
        query: UserFilterQuery,
        pagination: Pageable,
        authentication: Authentication
    ): ResponseEntity<*> {
        val output = getUserListUseCase(
            GetUserListUseCase.Input(
                credentials = authentication.getUserKey(),
                pagination = pagination,
                filterDto = query.getDto(),
            )
        )

        val httpHeaders: HttpHeaders = HttpHeaders().apply {
            set("X-Total-Count", output.totalElements.toString())
        }

        return ResponseEntity
            .ok()
            .headers(httpHeaders)
            .body(
                Response(
                    data = ResponseList(
                        items = output.users.map { UserResponse(dto = it) },
                        totalElements = output.totalElements,
                        totalPages = output.totalPages,
                    )
                )
            )
    }

    @PutMapping("/{userId}")
    fun updateUser(
        @RequestBody
        @Valid
        request: UpdateUserRequest,
        @PathVariable(name = "userId")
        @NotBlank
        @org.hibernate.validator.constraints.UUID
        userId: String,
        authentication: Authentication
    ): ResponseEntity<*> {
        updateUserUseCase.invoke(
            UpdateUserUseCase.Input(
                id = UUID.fromString(userId),
                updateDto = request.getDto(),
                credentials = authentication.getUserKey()
            )
        )

        return ResponseEntity.ok(EmptyResponse())
    }

    @GetMapping("/{userId}")
    fun details(
        @PathVariable(name = "userId")
        @NotBlank
        @org.hibernate.validator.constraints.UUID
        userId: String,
        authentication: Authentication
    ): ResponseEntity<*> {
        val userDto: UserDto = getUserUseCase.invoke(
            GetUserUseCase.Input(
                id = UUID.fromString(userId),
                credentials = authentication.getUserKey()
            )
        )

        return ResponseEntity.ok(Response(data = UserResponse(userDto)))
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(
        @PathVariable(name = "userId")
        @NotBlank
        @org.hibernate.validator.constraints.UUID
        userId: String,
        authentication: Authentication
    ): ResponseEntity<*> {
        deleteUserUseCase.invoke(
            DeleteUserUseCase.Input(
                id = UUID.fromString(userId),
                credentials = authentication.getUserKey()
            )
        )

        return ResponseEntity.ok(EmptyResponse())
    }

    @PutMapping("/{userId}/block")
    fun blockUser(
        @PathVariable(name = "userId")
        @NotBlank
        @org.hibernate.validator.constraints.UUID
        userId: String,
        authentication: Authentication
    ): ResponseEntity<*> {
        changeUserStatusUseCase.invoke(
            ChangeUserStatusUseCase.Input(
                id = UUID.fromString(userId),
                status = Status.BLOCKED,
                credentials = authentication.getUserKey()
            )
        )

        return ResponseEntity.ok(EmptyResponse())
    }

    @PutMapping("/{userId}/unblock")
    fun unblockUser(
        @PathVariable(name = "userId")
        @NotBlank
        @org.hibernate.validator.constraints.UUID
        userId: String,
        authentication: Authentication
    ): ResponseEntity<*> {
        changeUserStatusUseCase.invoke(
            ChangeUserStatusUseCase.Input(
                id = UUID.fromString(userId),
                status = Status.ACTIVE,
                credentials = authentication.getUserKey()
            )
        )

        return ResponseEntity.ok(EmptyResponse())
    }
}
