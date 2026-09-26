package com.icerockdev.notification.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.icerockdev.api.request.BaseRequest
import jakarta.validation.constraints.NotNull

@JsonIgnoreProperties(ignoreUnknown = true)
internal class PushTokenRequest(
    @field:NotNull(message = "Token is required")
    var token: String
) : BaseRequest()
