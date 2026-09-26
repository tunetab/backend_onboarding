package com.icerockdev.application.auth.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.icerockdev.api.request.BaseRequest
import com.icerockdev.application.common.Constants
import com.icerockdev.i18n.LK
import com.icerockdev.validation.annotation.FieldMatch
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@FieldMatch(message = "{${LK.passwordMustMatchPasswordRepeat}}", first = "password", second = "passwordRepeat")
@JsonIgnoreProperties(ignoreUnknown = true)
data class ChangeRestorePasswordRequest(
    @field:NotNull(message = "{${LK.confirmationTokenIsRequired}}")
    val confirmationToken: String,

    @field:Size(message = "{${LK.passwordMinLength}}", min = 6)
    @field:NotNull(message = "{${LK.passwordIsRequired}}")
    val password: String,

    @field:Size(message = "{${LK.passwordMinLength}}", min = 6)
    @field:NotNull(message = "{${LK.passwordIsRequired}}")
    val passwordRepeat: String,

    @field:Pattern(message = "{${LK.phoneIsIncorrect}}", regexp = Constants.PHONE_NUMBER)
    val phone: String?
) : BaseRequest()
