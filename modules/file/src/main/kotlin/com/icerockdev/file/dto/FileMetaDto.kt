package com.icerockdev.file.dto

data class FileMetaDto(
    val byteSize: Long = 0,
    val originalName: String? = null,
    val mimeType: String? = null,
)
