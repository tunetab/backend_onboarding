package com.icerockdev.application.user.request

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.icerockdev.api.request.BaseRequest
import com.icerockdev.application.common.Constants
import com.icerockdev.application.user.dto.CreateUserDto
import com.icerockdev.application.user.model.Status
import com.icerockdev.auth.acl.Role
import com.icerockdev.i18n.LK
import com.icerockdev.validation.annotation.StrictEmail
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@JsonIgnoreProperties(ignoreUnknown = true)
data class CreateUserRequest(
    @field:StrictEmail(message = "{${LK.emailIsNotValid}}")
    @field:NotNull(message = "{${LK.emailIsRequired}}")
    val email: String,

    @field:Pattern(message = "{${LK.phoneIsIncorrect}}", regexp = Constants.PHONE_NUMBER)
    @field:NotNull(message = "{${LK.phoneIsRequired}}")
    val phone: String,

    @field:NotNull(message = "{${LK.passwordIsRequired}}")
    @field:Size(min = Constants.MIN_PASSWORD, max = Constants.MAX_PASSWORD)
    val password: String,

    val isTest: Boolean? = false,

    @field:NotNull(message = "{${LK.roleIsRequired}}")
    val role: Role
) : BaseRequest() {

    @JsonIgnore
    fun getDto(): CreateUserDto {
        return CreateUserDto(
            email = email,
            phone = phone,
            password = password,
            isTest = requireNotNull(isTest),
            status = Status.ACTIVE,
            role = role
        )
    }
}
