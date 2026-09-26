package com.icerockdev.notification.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.icerockdev.api.request.BaseRequest
import jakarta.validation.constraints.Min

@JsonIgnoreProperties(ignoreUnknown = true)
internal class NotificationFilterRequest(
    @field:Min(message = "minimal value for type is 1", value = 1)
    val type: Int? = null,
    val isRead: Boolean? = null
) : BaseRequest()
