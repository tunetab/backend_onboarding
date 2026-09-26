package com.icerockdev.file.usecase

import com.icerockdev.service.storage.s3.IS3Storage
import com.icerockdev.service.storage.s3.policy.dto.Principal
import com.icerockdev.service.storage.s3.policy.dto.Statement

import org.springframework.stereotype.Component

@Component
internal class CreateBucketIfNotExistUseCase(
    private val storage: IS3Storage
) {

    fun execute(bucketName: String) {
        if (!storage.bucketExist(bucketName)) {
            storage.createBucket(bucketName)
            storage.putBucketPolicy(bucketName, false) {
                sid = "AddPerm"
                statement = listOf(
                    Statement(
                        effect = "Allow",
                        principal = Principal(
                            aws = listOf("*"),
                            canonicalUser = null,
                            federated = null,
                            service = listOf("*")
                        ),
                        notPrincipal = null,
                        condition = null,
                        action = listOf("s3:GetObject"),
                        resource = listOf("arn:aws:s3:::*")
                    )
                ).toMutableList()
            }
        }
    }
}
