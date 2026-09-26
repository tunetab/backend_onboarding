package com.icerockdev.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object Serializer {
    val objectMapper: ObjectMapper = jacksonObjectMapper().apply {
        applyDefaultConfiguration()
        configure(SerializationFeature.INDENT_OUTPUT, false)
    }

    fun serialize(obj: Any?): String {
        return objectMapper.writeValueAsString(obj)
    }

    inline fun <reified T> deserialize(content: String): T {
        return objectMapper.readValue(content = content)
    }
}
