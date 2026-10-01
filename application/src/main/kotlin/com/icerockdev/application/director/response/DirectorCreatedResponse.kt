package com.icerockdev.application.director.response

import com.icerockdev.api.response.Response

class DirectorCreatedResponse(
    data: DirectorResponse,
    val message: String,
) : Response<DirectorResponse>(success = true, data = data)
