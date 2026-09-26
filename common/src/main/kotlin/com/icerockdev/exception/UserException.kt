/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.exception

import com.icerockdev.api.response.ErrorListResponse

abstract class UserException(
    val status: Int,
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause) {
    open fun getErrorResponse(): ErrorListResponse {
        return ErrorListResponse(
            listOf(
                ErrorDetail(
                    message = requireNotNull(this.message),
                )
            )
        )
    }
}
