package com.icerockdev.application.director.controller

import com.icerockdev.application.director.request.CreateDirectorRequest
import com.icerockdev.application.director.response.DirectorCreatedResponse
import com.icerockdev.application.director.response.DirectorListData
import com.icerockdev.application.director.response.DirectorListResponse
import com.icerockdev.application.director.response.DirectorResponse
import com.icerockdev.application.director.usecase.CreateDirectorUseCase
import com.icerockdev.application.director.usecase.GetDirectorListUseCase
import com.icerockdev.i18n.I18N
import com.icerockdev.i18n.LK
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/user/directors")
class DirectorController(
    private val createDirectorUseCase: CreateDirectorUseCase,
    private val getDirectorListUseCase: GetDirectorListUseCase,
    private val i18N: I18N,
) {
    @Operation(
        summary = "Create director",
        description = "Creates a director and returns DirectorCreatedResponse.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "DirectorCreatedResponse",
                content = [Content(schema = Schema(implementation = DirectorCreatedResponse::class))],
            ),
        ],
    )
    @PostMapping("")
    fun create(
        @RequestBody @Valid request: CreateDirectorRequest,
    ): ResponseEntity<DirectorCreatedResponse> {
        val director = DirectorResponse(createDirectorUseCase(CreateDirectorUseCase.Input(request)))
        val messageKey = if (request.winOscar == true) {
            listOf(
                LK.directorCreatedGoodChoice,
                LK.directorCreatedLove,
                LK.directorCreatedBest,
            ).random()
        } else {
            LK.directorCreatedUnrecognized
        }

        return ResponseEntity.ok(DirectorCreatedResponse(director, i18N.t(messageKey)))
    }

    @Operation(
        summary = "Get directors",
        description = "Returns DirectorListResponse in the data field.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "DirectorListResponse",
                content = [Content(schema = Schema(implementation = DirectorListResponse::class))],
            ),
        ],
    )
    @GetMapping("")
    fun list(pageable: Pageable): ResponseEntity<DirectorListResponse> {
        val output = getDirectorListUseCase(GetDirectorListUseCase.Input(pageable))
        val headers = HttpHeaders().apply {
            set("X-Total-Count", output.totalElements.toString())
        }
        return ResponseEntity.ok()
            .headers(headers)
            .body(
                DirectorListResponse(
                    DirectorListData(
                        directors = output.directors.map(::DirectorResponse),
                        totalElements = output.totalElements,
                        totalPages = output.totalPages,
                    )
                )
            )
    }
}
