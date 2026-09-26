package com.icerockdev.application.auth.request

import com.fasterxml.jackson.annotation.JsonIgnore
import com.icerockdev.api.request.BaseRequest
import com.icerockdev.application.common.Constants
import com.icerockdev.application.user.dto.UserDto
import com.icerockdev.application.user.model.Status
import com.icerockdev.auth.acl.Role
import com.icerockdev.i18n.LK
import com.icerockdev.validation.annotation.StrictEmail
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class SignUpRequest(
    @field:StrictEmail(message = "{${LK.emailIsNotValid}}")
    @field:NotNull(message = "{${LK.emailIsRequired}}")
    val email: String,

    @field:Pattern(message = "{${LK.phoneIsIncorrect}}", regexp = Constants.PHONE_NUMBER)
    @field:NotNull(message = "{${LK.phoneIsRequired}}")
    val phone: String,

    @field:Size(message = "{${LK.passwordMinLength}}", min = 6)
    @field:NotNull(message = "{${LK.passwordIsRequired}}")
    val password: String
) : BaseRequest() {
    @JsonIgnore
    fun getDto(): UserDto {
        return UserDto(
            phone = phone,
            email = email,
            password = password,
            isTest = false,
            status = Status.ACTIVE,
            role = Role.USER,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }
}
