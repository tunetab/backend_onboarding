package com.icerockdev.application.auth.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.icerockdev.api.request.BaseRequest
import com.icerockdev.i18n.LK
import com.icerockdev.validation.annotation.StrictEmail
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@JsonIgnoreProperties(ignoreUnknown = true)
data class SignInRequest(
    @field:StrictEmail(message = "{${LK.emailIsNotValid}}")
    @field:NotNull(message = "{${LK.emailIsRequired}}")
    val email: String,

    @field:Size(message = "{${LK.passwordMinLength}}", min = 6)
    @field:NotNull(message = "{${LK.passwordIsRequired}}")
    val password: String
) : BaseRequest()
