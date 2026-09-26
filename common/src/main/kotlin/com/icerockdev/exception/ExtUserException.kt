/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.exception

import com.icerockdev.api.response.ErrorList
import com.icerockdev.api.response.ErrorListResponse

abstract class ExtUserException(status: Int, message: String) : UserException(status, message) {
    var data: List<ErrorDetail> = mutableListOf()

    override fun getErrorResponse(): ErrorListResponse {
        return ErrorListResponse().also {
            it.data = ErrorList(data).takeIf { errorsData -> errorsData.items.isNotEmpty() }
                ?: ErrorList(
                    listOf(
                        ErrorDetail(
                            message = requireNotNull(message),
                        )
                    )
                )
        }
    }

    protected fun setErrors(list: List<ErrorDetail>) {
        this.data = list
    }
}
