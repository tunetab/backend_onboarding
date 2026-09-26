/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.api.response

import com.icerockdev.util.DateTimeUtil

open class Response<T>(
    var success: Boolean = false,
    var timestamp: Long = DateTimeUtil.getTimestamp(),
    var data: T? = null,
)
