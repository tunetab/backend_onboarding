package com.icerockdev.file.config

import com.icerockdev.service.storage.s3.IS3Storage
import com.icerockdev.service.storage.s3.S3StorageImpl
import com.icerockdev.service.storage.s3.s3Configuration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.net.URI
import javax.activation.MimeType

@Configuration
@EnableScheduling
class FileConfig(
    private val s3Settings: S3Settings
) {

    @Bean("allowedMimeTypes")
    fun getAllowedMimeTypes(): List<MimeType> {
        val list: List<String> = s3Settings.allowedMimeTypes.split(",")

        return list.map { MimeType(it) }
    }

    @Bean("bucketName")
    fun getBucketName(): String {
        return s3Settings.bucketName
    }

    @Bean("endpoint")
    fun getEndpoint(): String {
        return s3Settings.endpoint
    }

    @Bean
    fun getS3Storage(): IS3Storage {
        val client = S3Client.builder()
            .serviceConfiguration(s3Configuration)
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(s3Settings.accessKeyId, s3Settings.secretAccessKey)
                )
            )
            .endpointOverride(URI.create(s3Settings.endpoint))
            .region(Region.of(s3Settings.region))
            .build()

        val preSigner = S3Presigner.builder()
            .serviceConfiguration(s3Configuration)
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(s3Settings.accessKeyId, s3Settings.secretAccessKey)
                )
            )
            .endpointOverride(URI.create(s3Settings.endpoint))
            .region(Region.of(s3Settings.region))
            .build()

        return S3StorageImpl(
            client = client,
            preSigner = preSigner
        )
    }
}
