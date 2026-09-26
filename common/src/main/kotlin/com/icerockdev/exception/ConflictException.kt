package com.icerockdev.exception

import org.springframework.http.HttpStatus

class ConflictException(
    message: String = HttpStatus.CONFLICT.reasonPhrase
) : UserException(HttpStatus.CONFLICT.value(), message)
