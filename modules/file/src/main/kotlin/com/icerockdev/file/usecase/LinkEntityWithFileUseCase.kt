package com.icerockdev.file.usecase

import com.icerockdev.exception.NotFoundException
import com.icerockdev.file.abstract.IFileType
import com.icerockdev.file.repository.FileRepository
import com.icerockdev.i18n.I18N
import com.icerockdev.i18n.LK
import java.util.UUID
import org.springframework.stereotype.Component

@Component
internal class LinkEntityWithFileUseCase(
    private val fileRepository: FileRepository,
    private val i18N: I18N
) {

    fun execute(
        key: String,
        entityId: UUID,
        entityType: IFileType
    ): UUID {
        val file = fileRepository.findByKey(key = key) ?: throw NotFoundException(i18N.t(LK.fileNotFound))

        return fileRepository.save(
            file.copy(
                entityId = entityId,
                entityType = entityType.getType()
            )
        ).id
    }
}
