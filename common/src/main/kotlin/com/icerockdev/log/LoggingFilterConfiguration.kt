package com.icerockdev.log

import org.springframework.http.MediaType

data class LoggingFilterConfiguration(
    /**
     * List of MediaType's whose values should be logged.
     */
    var visibleMediaTypes: List<MediaType>,
    /**
     * List of HTTP headers whose values should not be logged.
     */
    var sensitiveHeaders: List<String>,
    /**
     * List of fields whose values should not be logged.
     */
    var sensitiveFields: List<String>
)
