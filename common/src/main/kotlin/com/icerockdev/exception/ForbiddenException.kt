/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.exception

import org.springframework.http.HttpStatus

class ForbiddenException(
    message: String = HttpStatus.FORBIDDEN.reasonPhrase
) : UserException(HttpStatus.FORBIDDEN.value(), message)
