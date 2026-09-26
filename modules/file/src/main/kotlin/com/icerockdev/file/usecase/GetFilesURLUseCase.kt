package com.icerockdev.file.usecase

import com.icerockdev.file.abstract.IFileType
import com.icerockdev.file.dto.FileDto
import com.icerockdev.file.repository.FileRepository
import java.util.UUID
import org.springframework.stereotype.Component

@Component
internal class GetFilesURLUseCase(
    private val bucketName: String,
    private val fileRepository: FileRepository,
    private val getObjectURLUseCase: GetObjectURLUseCase
) {

    fun execute(
        entityIdList: List<UUID>,
        entityType: IFileType,
    ): Map<UUID?, FileDto> {
        return fileRepository.findAllByEntityIdInAndEntityType(
            entityIdList = entityIdList,
            entityType = entityType.getType()
        ).map { file ->
            FileDto(
                key = file.key,
                entityId = file.entityId,
                entityType = file.entityType,
                meta = file.meta,
                imageUrl = getObjectURLUseCase.execute(bucketName, file.key),
                createdAt = file.createdAt,
                updatedAt = file.updatedAt
            )
        }.filter { file -> file.entityId != null }.associateBy { file -> file.entityId }
    }
}
