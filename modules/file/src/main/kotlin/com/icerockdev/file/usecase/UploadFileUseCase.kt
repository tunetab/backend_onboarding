package com.icerockdev.file.usecase

import com.icerockdev.exception.ServerErrorException
import com.icerockdev.file.common.Constants
import com.icerockdev.file.common.getMimeType
import com.icerockdev.file.common.validateFileSize
import com.icerockdev.file.common.validateMimeType
import com.icerockdev.file.dto.FileMetaDto
import com.icerockdev.file.model.File
import com.icerockdev.file.repository.FileRepository
import com.icerockdev.i18n.I18N
import com.icerockdev.i18n.LK
import java.time.LocalDateTime
import java.util.UUID
import javax.activation.MimeType
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile


@Component
internal class UploadFileUseCase(
    private val allowedMimeTypes: List<MimeType>,
    private val bucketName: String,
    private val fileRepository: FileRepository,
    private val putObjectUseCase: PutObjectUseCase,
    private val createBucketIfNotExistUseCase: CreateBucketIfNotExistUseCase,
    private val deleteObjectIfExistUseCase: DeleteObjectIfExistUseCase,
    private val i18N: I18N
) {

    fun execute(multipartFile: MultipartFile): String {
        val fileMetaDto = FileMetaDto(
            byteSize = multipartFile.size,
            originalName = multipartFile.originalFilename,
            mimeType = multipartFile.inputStream.use { getMimeType(it) }
        )

        validateFileSize(
            fileSize = fileMetaDto.byteSize,
            maxByteSize = Constants.MAX_FILE_UPLOAD_BYTES,
            message = i18N.t(LK.fileMaxSize).replace("{max_size}", Constants.MAX_FILE_UPLOAD_BYTES.toString())
        )

        validateMimeType(
            type = fileMetaDto.mimeType?.let { MimeType(it) },
            validTypes = allowedMimeTypes,
            message = i18N.t(LK.fileInvalidType)
                .replace("{types}", allowedMimeTypes.joinToString(i18N.t(LK.fileInvalidTypeOr)) { it.toString() })
        )

        createBucketIfNotExistUseCase.execute(bucketName)

        val key: String = multipartFile.inputStream.use { stream ->
            stream.buffered().use { putObjectUseCase.execute(bucketName, it, fileMetaDto) }
        } ?: throw ServerErrorException(message = i18N.t(LK.fileSaveError))

        val file = File(
            id = UUID.randomUUID(),
            key = key,
            meta = fileMetaDto,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        try {
            fileRepository.save(file)
        } catch (t: Throwable) {
            deleteObjectIfExistUseCase.execute(
                bucketName = bucketName,
                key = key
            )
            throw t
        }

        return key
    }
}
