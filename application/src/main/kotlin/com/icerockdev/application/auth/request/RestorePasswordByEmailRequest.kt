package com.icerockdev.application.auth.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.icerockdev.api.request.BaseRequest
import com.icerockdev.i18n.LK
import com.icerockdev.validation.annotation.StrictEmail
import jakarta.validation.constraints.NotNull

@JsonIgnoreProperties(ignoreUnknown = true)
data class RestorePasswordByEmailRequest(
    @field:StrictEmail(message = "{${LK.emailIsNotValid}}")
    @field:NotNull(message = "{${LK.emailIsRequired}}")
    val email: String
) : BaseRequest()
