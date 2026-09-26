package com.icerockdev.api.response

import com.icerockdev.exception.ErrorDetail

data class ErrorList(
    var items: List<ErrorDetail> = listOf(),
)
