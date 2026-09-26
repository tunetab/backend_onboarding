/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.exception

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.http.HttpStatus

class BadRequestException(
    message: String = HttpStatus.BAD_REQUEST.reasonPhrase
) : ExtUserException(HttpStatus.BAD_REQUEST.value(), message) {
    constructor(exception: MissingKotlinParameterException) : this() {
        val fieldName = exception.path.joinToString(separator = ".") { it.fieldName ?: "[]" }
        setErrors(listOf(ErrorDetail(message = "Property $fieldName are required", field = fieldName)))
    }

    constructor(errorList: List<ErrorDetail>) : this() {
        setErrors(errorList)
    }
}
