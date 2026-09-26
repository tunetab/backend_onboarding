package com.icerockdev.file.usecase

import com.icerockdev.file.dto.FileMetaDto
import com.icerockdev.service.storage.s3.IS3Storage
import com.icerockdev.service.storage.s3.dto.FileObjectDto
import java.io.BufferedInputStream
import org.springframework.stereotype.Component

@Component
internal class PutObjectUseCase(private val storage: IS3Storage) {

    fun execute(bucketName: String, stream: BufferedInputStream, fileMetaDto: FileMetaDto): String? {
        // New object key
        val key = storage.generateFileKey()
        val metadata = fileMetaDto.originalName?.let {
            mapOf("original-name" to it)
        }
        if (storage.put(bucketName, key, FileObjectDto(inputStream = stream, size = fileMetaDto.byteSize), metadata)) {
            return key
        }

        return null
    }
}
