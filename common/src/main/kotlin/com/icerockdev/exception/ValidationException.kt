/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.exception

import com.icerockdev.api.request.Request
import jakarta.validation.ConstraintViolation
import org.springframework.http.HttpStatus

class ValidationException(message: String = HttpStatus.UNPROCESSABLE_ENTITY.reasonPhrase) :
    ExtUserException(HttpStatus.UNPROCESSABLE_ENTITY.value(), message) {

    constructor(constraintViolationList: Set<ConstraintViolation<Request>>) : this() {
        val errorList = mutableListOf<ErrorDetail>()
        for (constraintViolation in constraintViolationList) {
            errorList.add(
                ErrorDetail(
                    message = constraintViolation.message,
                    field = constraintViolation.propertyPath?.lastOrNull()?.name
                )
            )
        }
        setErrors(errorList)
    }

    constructor(errorList: List<ErrorDetail>) : this() {
        setErrors(errorList)
    }
}
