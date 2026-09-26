package com.icerockdev.file.common

import com.icerockdev.exception.ValidationException
import com.icerockdev.service.storage.mime.MimeTypeDetector
import java.io.InputStream
import javax.activation.MimeType

internal fun validateFileSize(fileSize: Long, maxByteSize: Long, message: String) {
    if (fileSize > maxByteSize) {
        throw ValidationException(message)
    }
}

internal fun validateMimeType(type: MimeType?, validTypes: List<MimeType>, message: String) {
    if (type == null || validTypes.none { it.match(type) }) {
        throw ValidationException(message)
    }
}

fun getMimeType(inputStream: InputStream): String {
    return MimeTypeDetector.detect(inputStream).toString()
}
