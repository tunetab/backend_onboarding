/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.api.response

data class ResponseList<T>(
    var items: List<T> = listOf(),
    var totalElements: Int = 0,
    var totalPages: Int = 0,
)
