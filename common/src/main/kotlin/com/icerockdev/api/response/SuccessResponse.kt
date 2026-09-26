package com.icerockdev.api.response

import org.springframework.http.HttpStatus

data class SuccessResponse(
    val data: Any? = null
) : AbstractResponse(status = HttpStatus.OK.value(), message = "ok", success = true)
