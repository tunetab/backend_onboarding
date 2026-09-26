package com.icerockdev.file.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "s3storage")
class S3Settings {
    lateinit var bucketName: String
    lateinit var accessKeyId: String
    lateinit var secretAccessKey: String
    lateinit var endpoint: String
    lateinit var region: String
    lateinit var allowedMimeTypes: String
}
