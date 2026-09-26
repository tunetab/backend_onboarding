/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.exception

import org.springframework.http.HttpStatus


class ServerErrorException(
    message: String = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
    cause: Throwable? = null
) : UserException(
    status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
    message = message,
    cause = cause
)
