package com.icerockdev.api.response

import com.icerockdev.exception.ErrorDetail
import jakarta.validation.ConstraintViolation
import kotlin.collections.lastOrNull
import kotlin.collections.map

class ErrorListResponse() : Response<ErrorList>() {
    constructor(constraintViolationList: Set<ConstraintViolation<*>>) : this() {
        setValidationParams(constraintViolationList.map {
            ErrorDetail(message = it.message, field = it.propertyPath.lastOrNull()?.name)
        })
    }

    constructor(errorList: List<ErrorDetail>) : this() {
        setValidationParams(errorList)
    }

    private fun setValidationParams(list: List<ErrorDetail>) {
        this.data = ErrorList(list)
    }
}
