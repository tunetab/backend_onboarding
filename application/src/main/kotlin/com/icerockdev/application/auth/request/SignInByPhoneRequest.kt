package com.icerockdev.application.auth.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.icerockdev.api.request.BaseRequest
import com.icerockdev.application.common.Constants
import com.icerockdev.i18n.LK
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@JsonIgnoreProperties(ignoreUnknown = true)
data class SignInByPhoneRequest(
    @field:Pattern(message = "{${LK.phoneIsIncorrect}}", regexp = Constants.PHONE_NUMBER)
    @field:NotNull(message = "{${LK.phoneIsRequired}}")
    val phone: String,

    @field:Size(message = "{${LK.passwordMinLength}}", min = 6)
    @field:NotNull(message = "{${LK.passwordIsRequired}}")
    val password: String
) : BaseRequest()
