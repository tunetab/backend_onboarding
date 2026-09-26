package com.icerockdev.application.user.request

import com.fasterxml.jackson.annotation.JsonIgnore
import com.icerockdev.api.request.BaseRequest
import com.icerockdev.application.common.Constants
import com.icerockdev.application.user.dto.UpdateUserDto
import com.icerockdev.application.user.model.Status
import com.icerockdev.auth.acl.Role
import com.icerockdev.i18n.LK
import com.icerockdev.validation.annotation.StrictEmail
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class UpdateUserRequest(
    @field:StrictEmail(message = "{${LK.emailIsNotValid}}")
    val email: String? = null,

    @field:Pattern(message = "{${LK.phoneIsIncorrect}}", regexp = Constants.PHONE_NUMBER)
    val phone: String? = null,

    @field:Size(min = Constants.MIN_PASSWORD, max = Constants.MAX_PASSWORD)
    val password: String? = null,

    val role: Role? = null,

    val status: Status? = Status.ACTIVE,

    val isTest: Boolean? = false,
) : BaseRequest() {
    @JsonIgnore
    fun getDto(): UpdateUserDto {
        return UpdateUserDto(
            email = email,
            phone = phone,
            password = password,
            role = role,
            status = status,
            isTest = isTest,
            updatedAt = LocalDateTime.now()
        )
    }
}
