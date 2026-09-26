package com.icerockdev.file.usecase

import com.icerockdev.service.storage.s3.IS3Storage
import org.springframework.stereotype.Component

@Component
internal class DeleteObjectIfExistUseCase(private val storage: IS3Storage) {

    fun execute(bucketName: String, key: String?): Boolean {
        if (key == null) {
            return true
        }

        if (storage.objectExists(bucketName, key)) {
            return storage.delete(bucketName, key)
        }

        return false
    }
}
