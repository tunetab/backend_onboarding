/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.api.response

import com.icerockdev.exception.ErrorDetail

class ErrorResponse(error: ErrorDetail? = null) : Response<ErrorDetail>(data = error)

