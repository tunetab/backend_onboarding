package com.icerockdev.application.tests.modules.file

import com.icerockdev.application.tests.common.api.client.ApiClient
import com.icerockdev.application.tests.common.api.client.response.ResponseList
import com.icerockdev.application.tests.common.db.annotations.SpringBootTestWithDB
import com.icerockdev.application.tests.fixture.AuthFixtureBuilder
import com.icerockdev.application.tests.fixture.UserFixtureBuilder
import com.icerockdev.application.tests.modules.file.api.FileApi
import com.icerockdev.application.user.model.User
import com.icerockdev.auth.acl.Role
import com.icerockdev.file.tests.fixture.ImageFixtureBuilder
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.openapitools.client.models.ErrorDetail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile

@SpringBootTestWithDB
class FileApiTests {
    private lateinit var user: User

    @Autowired
    private lateinit var fileApi: FileApi

    @Autowired
    private lateinit var authFixtureBuilder: AuthFixtureBuilder

    @Autowired
    private lateinit var userFixtureBuilder: UserFixtureBuilder

    @Autowired
    private lateinit var imageFixtureBuilder: ImageFixtureBuilder

    @BeforeEach
    fun initUser() {
        user = userFixtureBuilder.buildUser(
            email = "user@project.test",
            phone = "71234567890",
            password = "12345678",
            role = Role.USER
        )
        fileApi.accessToken = authFixtureBuilder.authorizeUser(user.email, "12345678").accessToken
    }

    @Test
    fun uploadFileTest() {
        val response = fileApi.uploadFile<String>(
            ApiClient.RequestBody(
                MockMultipartFile(
                    "file",
                    "test.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    imageFixtureBuilder.buildJpgImage()
                )
            )
        )

        assertDoesNotThrow {
            UUID.fromString(response.data.replace("/", "-"))
        }
    }

    @Test
    fun uploadUnsupportedFileTest() {
        val response = fileApi.uploadFile<ResponseList<ErrorDetail>>(
            ApiClient.RequestBody(
                MockMultipartFile(
                    "file",
                    "test.txt",
                    MediaType.TEXT_PLAIN_VALUE,
                    "Test".byteInputStream()
                )
            )
        ) {
            isUnprocessableEntity()
        }

        assertFalse(response.success)

        val errorsMessages = response.data.items.map { it.message }

        assertTrue {
            errorsMessages.any {
                it.contains("File should be one of types:") ||
                        it.contains("Файл должен быть одного из типов:")
            }
        }
    }
}
