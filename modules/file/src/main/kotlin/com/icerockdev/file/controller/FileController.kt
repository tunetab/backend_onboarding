package com.icerockdev.file.controller

import com.icerockdev.api.response.Response
import com.icerockdev.exception.UnauthorizedException
import com.icerockdev.file.usecase.UploadFileUseCase
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Validated
@RestController
@RequestMapping("/v1/file")
internal class FileController(
    private val uploadFileUseCase: UploadFileUseCase
) {

    @PostMapping("/upload")
    fun upload(
        @RequestParam("file")
        file: MultipartFile,
        authentication: Authentication
    ): ResponseEntity<*> {
        if (!authentication.isAuthenticated) {
            throw UnauthorizedException()
        }

        val key: String = uploadFileUseCase.execute(multipartFile = file)

        return ResponseEntity.ok(Response(data = key))
    }
}
