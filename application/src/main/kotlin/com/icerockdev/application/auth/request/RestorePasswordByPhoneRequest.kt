package com.icerockdev.application.auth.request

import com.icerockdev.application.common.Constants
import com.icerockdev.i18n.LK
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class RestorePasswordByPhoneRequest(
    @field:Pattern(message = "{${LK.phoneIsIncorrect}}", regexp = Constants.PHONE_NUMBER)
    @field:NotNull(message = "{${LK.phoneIsRequired}}")
    val phone: String
)
