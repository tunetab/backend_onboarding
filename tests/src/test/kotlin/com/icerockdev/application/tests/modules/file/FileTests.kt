package com.icerockdev.application.tests.modules.file

import com.icerockdev.application.tests.common.db.annotations.SpringBootTestWithDB
import com.icerockdev.file.FileService
import com.icerockdev.file.tests.fixture.FileFixtureBuilder
import com.icerockdev.file.tests.fixture.TestFileType
import java.time.Duration
import java.util.UUID
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired

@SpringBootTestWithDB
class FileTests {

    @Autowired
    private lateinit var fileFixtureBuilder: FileFixtureBuilder

    @Autowired
    private lateinit var fileService: FileService

    @Test
    fun linkFileWithEntityTest() {
        val key = fileFixtureBuilder.buildFile()
        assertDoesNotThrow {
            fileService.linkEntityWithFile(
                key = key,
                entityId = UUID.randomUUID(),
                entityType = TestFileType.TEST_FILE
            )
        }
    }

    @Test
    fun unlinkFileWithEntityTest() {
        val entityId = UUID.randomUUID()
        val entityType = TestFileType.TEST_SECOND_FILE
        fileFixtureBuilder.buildLinkedWithEntityFile(entityId = entityId, entityType = entityType)

        val count = assertDoesNotThrow {
            fileService.unlinkEntityFromFile(entityId = entityId, entityType = entityType)
        }
        assertEquals(expected = 1, actual = count)
    }

    @Test
    fun unlinkFileWithEntityByKeyTest() {
        val key = fileFixtureBuilder.buildLinkedWithEntityFile(
            entityId = UUID.randomUUID(),
            entityType = TestFileType.TEST_SECOND_FILE
        )

        val count = assertDoesNotThrow {
            fileService.unlinkEntityFromFileByKey(listOf(key))
        }

        assertEquals(expected = 1, actual = count)
    }

    @Test
    fun getFilesDtoTest() {
        val firstEntityId = UUID.randomUUID()
        val secondEntityId = UUID.randomUUID()
        fileFixtureBuilder.buildLinkedWithEntityFile(entityId = firstEntityId, TestFileType.TEST_FILE)
        fileFixtureBuilder.buildLinkedWithEntityFile(entityId = secondEntityId, TestFileType.TEST_FILE)

        val fileDtoList = fileService.getFilesDto(listOf(firstEntityId, secondEntityId), TestFileType.TEST_FILE)

        assertEquals(expected = 2, actual = fileDtoList.count())
        assertContains(fileDtoList, firstEntityId)
        assertContains(fileDtoList, secondEntityId)
    }

    @Test
    fun deleteFileByEntityTest() {
        val entityId = UUID.randomUUID()
        val entityType = TestFileType.TEST_SECOND_FILE
        fileFixtureBuilder.buildLinkedWithEntityFile(entityId = entityId, entityType = entityType)

        assertTrue {
            fileService.deleteFileByEntity(listOf(entityId), entityType)
        }
    }

    @Test
    fun shareFileTest() {
        val key = fileFixtureBuilder.buildFile()
        fileFixtureBuilder.putRandomImageInStorage(key)
        val url = assertDoesNotThrow {
            fileService.shareFile(
                bucketName = "test",
                key = key,
                Duration.ofDays(1)
            )
        }

        assertNotNull(url)
    }
}
