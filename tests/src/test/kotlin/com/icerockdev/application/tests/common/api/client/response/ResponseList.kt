package com.icerockdev.application.tests.common.api.client.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ResponseList<T>(
    var items: List<T> = listOf(),
    var totalElements: Int = 0,
    var totalPages: Int = 0,
)
