package com.icerockdev.file.usecase

import com.icerockdev.file.abstract.IFileType
import com.icerockdev.file.repository.FileRepository
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
internal class UnlinkEntityFromFileUseCase(
    private val fileRepository: FileRepository
) {

    @Transactional
    fun execute(
        entityId: UUID,
        entityType: IFileType,
    ): Int {
        val files = fileRepository.findAllByEntityIdAndEntityType(
            entityId = entityId,
            entityType = entityType.getType()
        )

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
