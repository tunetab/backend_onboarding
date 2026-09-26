package com.icerockdev.file.usecase

import com.icerockdev.file.abstract.IFileType
import com.icerockdev.file.repository.FileRepository
import java.util.UUID
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
internal class DeleteFileByEntityUseCase(
    private val deleteObjectIfExistUseCase: DeleteObjectIfExistUseCase,
    private val fileRepository: FileRepository,
    private val bucketName: String
) {

    @Transactional
    fun execute(
        entityIdList: List<UUID>,
        entityType: IFileType,
    ): Boolean = fileRepository.findAllByEntityIdInAndEntityType(
        entityIdList = entityIdList,
        entityType = entityType.getType()
    ).map { file ->
        fileRepository.deleteAllByKeyIn(listOf(file.key))
        deleteObjectIfExistUseCase.execute(bucketName, file.key)
    }.none { status -> !status }
}
