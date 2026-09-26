/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.exception

import org.springframework.http.HttpStatus

class NotFoundException(
    message: String = HttpStatus.NOT_FOUND.reasonPhrase
) : UserException(HttpStatus.NOT_FOUND.value(), message)
