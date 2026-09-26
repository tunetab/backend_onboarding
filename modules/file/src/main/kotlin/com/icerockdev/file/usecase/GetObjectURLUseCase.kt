package com.icerockdev.file.usecase

import com.icerockdev.service.storage.s3.IS3Storage
import java.net.URI
import org.springframework.stereotype.Component

@Component
internal class GetObjectURLUseCase(
    private val storage: IS3Storage,
    private val endpoint: String
) {

    fun execute(
        bucketName: String,
        key: String?
    ): String? {
        if (key == null || !storage.objectExists(bucketName, key)) {
            return null
        }

        return storage.getUrl(URI.create(endpoint), bucketName, key)
    }
}
