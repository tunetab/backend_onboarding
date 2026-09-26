package com.icerockdev.file.usecase

import com.icerockdev.file.repository.FileRepository
import java.time.LocalDateTime
import org.springframework.stereotype.Component

@Component
internal class UnlinkEntityFromFileByKeyUseCase(
    private val fileRepository: FileRepository
) {

    fun execute(keyList: List<String>): Int {
        val files = fileRepository.findAllByKeyIn(keyList)

        val updatedEntities = files.map { file ->
            file.copy(
                entityId = null,
                entityType = null,
                updatedAt = LocalDateTime.now()
            )
        }

        return fileRepository.saveAll(updatedEntities).size
    }
}
