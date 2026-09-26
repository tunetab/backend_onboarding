package com.icerockdev.api.response

import com.icerockdev.util.DateTimeUtil


open class AbstractResponse(
    var status: Int = 0,
    var message: String = "",
    var timestamp: Long = DateTimeUtil.getTimestamp(),
    var success: Boolean = false
)
