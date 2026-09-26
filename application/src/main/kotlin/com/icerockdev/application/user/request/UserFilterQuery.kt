package com.icerockdev.application.user.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.icerockdev.application.common.Constants
import com.icerockdev.application.user.dto.UserFilterDto
import com.icerockdev.application.user.model.Status
import com.icerockdev.auth.acl.Role
import com.icerockdev.i18n.LK
import com.icerockdev.validation.annotation.StrictEmail
import jakarta.validation.constraints.Pattern
import java.util.UUID

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserFilterQuery(
    @field:org.hibernate.validator.constraints.UUID
    val id: String? = null,
    @field:StrictEmail(message = "{${LK.emailIsNotValid}}")
    val email: String? = null,

    @field:Pattern(message = "{${LK.phoneIsIncorrect}}", regexp = Constants.PHONE_NUMBER)
    val phone: String? = null,

    val status: Status? = null,
    val role: Role? = null,
    val isTest: Boolean? = null
) {
    fun getDto() = UserFilterDto(
        id = id?.let { UUID.fromString(it) },
        phone = phone,
        email = email,
        status = status,
        role = role,
        isTest = isTest
    )
}
