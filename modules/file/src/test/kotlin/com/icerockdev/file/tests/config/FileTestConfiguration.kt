package com.icerockdev.file.tests.config

import com.icerockdev.service.storage.s3.IS3Storage
import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@TestConfiguration
class FileTestConfiguration {
    @Bean
    @Primary
    @Profile("!testRealS3")
    fun getTestS3Storage(): IS3Storage {
        val s3Storage = mockk<IS3Storage>()
        every { s3Storage.generateFileKey() } returns UUID.randomUUID().toString().replace("-", "/")
        every { s3Storage.objectExists(bucket = any(), key = any()) } returns true
        every { s3Storage.delete(bucket = any(), key = any()) } returns true
        every { s3Storage.bucketExist(bucket = any()) } returns true
        every { s3Storage.getUrl(endpoint = any(), bucket = any(), key = any()) } returns "http://test.test"
        every { s3Storage.put(bucket = any(), key = any(), file = any(), metadata = any()) } returns true
        every { s3Storage.put(bucket = any(), key = any(), stream = any(), metadata = any()) } returns true
        every { s3Storage.share(bucket = any(), key = any(), duration = any()) } returns "http://test.test"

        return s3Storage
    }
}
