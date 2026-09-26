package com.icerockdev.file.usecase

import com.icerockdev.service.storage.s3.IS3Storage
import java.time.Duration
import org.springframework.stereotype.Component

@Component
internal class ShareObjectUseCase(
    private val storage: IS3Storage
) {

    fun execute(bucketName: String, key: String?, duration: Duration): String? {
        if (key == null) {
            return null
        }

        if (!storage.objectExists(bucketName, key)) {
            return null
        }

        return storage.share(bucketName, key, duration)
    }
}
