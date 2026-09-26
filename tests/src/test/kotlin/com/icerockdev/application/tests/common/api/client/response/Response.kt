package com.icerockdev.application.tests.common.api.client.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.icerockdev.api.response.AbstractResponse

@JsonIgnoreProperties(ignoreUnknown = true)
data class Response<T>(
    val data: T
): AbstractResponse()
