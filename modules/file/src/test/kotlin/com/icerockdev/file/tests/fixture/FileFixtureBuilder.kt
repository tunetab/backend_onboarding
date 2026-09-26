package com.icerockdev.file.tests.fixture

import com.icerockdev.file.abstract.IFileType
import com.icerockdev.file.model.File
import com.icerockdev.file.repository.FileRepository
import com.icerockdev.service.storage.s3.IS3Storage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestComponent
import org.springframework.mock.web.MockMultipartFile
import java.time.LocalDateTime
import java.util.UUID

@TestComponent
class FileFixtureBuilder {

    @Autowired
    private lateinit var fileRepository: FileRepository

    @Autowired
    private lateinit var sIS3Storage: IS3Storage

    @Autowired
    private lateinit var imageFixtureBuilder: ImageFixtureBuilder

    @Autowired
    private lateinit var bucketName: String

    fun buildFile(): String {
        return fileRepository.save(
            File(
                id = UUID.randomUUID(),
                key = sIS3Storage.generateFileKey(),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        ).key
    }

    fun buildLinkedWithEntityFile(entityId: UUID, entityType: IFileType): String {
        return fileRepository.save(
            File(
                id = UUID.randomUUID(),
                key = sIS3Storage.generateFileKey(),
                entityId = entityId,
                entityType = entityType.getType(),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        ).also {
            sIS3Storage.put(
                bucketName,
                it.key,
                MockMultipartFile(UUID.randomUUID().toString(), imageFixtureBuilder.buildJpgImage()).inputStream
            )
        }.key
    }

    fun buildFileList(count: Int): List<String> {
        return (0 until count).map {
            buildFile()
        }
    }

    fun putRandomImageInStorage(key: String) {
        sIS3Storage.put(
            bucketName,
            key,
            MockMultipartFile(UUID.randomUUID().toString(), imageFixtureBuilder.buildJpgImage())
                .inputStream,
        )
    }
}
