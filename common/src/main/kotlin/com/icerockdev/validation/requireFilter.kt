package com.icerockdev.validation

import com.icerockdev.exception.ErrorDetail
import com.icerockdev.exception.ValidationException

fun <T : Any> requireFilter(value: T?, field: String? = null): T {
    return try {
        requireNotNull(value)
    } catch (e: IllegalArgumentException) {
        throw ValidationException(errorList = listOf(ErrorDetail(
            message = e.message ?: "",
            field = field
        )))
    }
}
