package com.icerockdev.application.auth.response

import com.icerockdev.api.response.AbstractResponse
import com.icerockdev.auth.jwt.JwtTokens
import org.springframework.http.HttpStatus

class TokenResponse(
    accessToken: String,
    refreshToken: String
) : AbstractResponse(status = HttpStatus.OK.value(), message = "ok", success = true) {
    val data: JwtTokens = JwtTokens(accessToken, refreshToken)
}

