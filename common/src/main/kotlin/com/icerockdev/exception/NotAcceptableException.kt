/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.exception

import org.springframework.http.HttpStatus

class NotAcceptableException(
    message: String = HttpStatus.NOT_ACCEPTABLE.reasonPhrase
) : UserException(HttpStatus.NOT_ACCEPTABLE.value(), message)
