package com.icerockdev.application.director.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@JsonIgnoreProperties(ignoreUnknown = true)
data class CreateDirectorRequest(
    @field:NotBlank(message = "Director name is required")
    val name: String,

    @field:NotBlank(message = "Director country is required")
    val country: String,

    @field:NotNull(message = "Oscar status is required")
    @field:JsonProperty("win_oscar")
    val winOscar: Boolean?,

    @field:JsonProperty("oscar_winner_title")
    val oscarWinnerTitle: String? = null,
)
