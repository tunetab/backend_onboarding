package com.icerockdev.file.usecase

import com.icerockdev.file.model.File
import com.icerockdev.file.repository.FileRepository
import java.time.LocalDateTime
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
internal class RemoveOldFilesUseCase(
    private val bucketName: String,
    private val fileRepository: FileRepository,
    private val deleteObjectIfExistUseCase: DeleteObjectIfExistUseCase
) {

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    fun execute() {
        val files: List<File> = fileRepository.getAllUnlinked(
            dateTime = LocalDateTime.now().minusHours(1)
        )

        files.forEach { file ->
            deleteObjectIfExistUseCase.execute(bucketName = bucketName, key = file.key)
            fileRepository.deleteByKey(file.key)
        }
    }
}
