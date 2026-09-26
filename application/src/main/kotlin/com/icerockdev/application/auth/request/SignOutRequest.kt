package com.icerockdev.application.auth.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.icerockdev.api.request.BaseRequest
import com.icerockdev.i18n.LK
import jakarta.validation.constraints.NotNull

@JsonIgnoreProperties(ignoreUnknown = true)
data class SignOutRequest(
    @field:NotNull(message = "{${LK.authTokenIsRequired}}")
    val refreshToken: String
) : BaseRequest()
