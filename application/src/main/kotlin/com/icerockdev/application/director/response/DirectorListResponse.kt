package com.icerockdev.application.director.response

import com.icerockdev.api.response.Response

class DirectorListResponse(
    data: DirectorListData,
) : Response<DirectorListData>(success = true, data = data)

data class DirectorListData(
    val directors: List<DirectorResponse>,
    val totalElements: Int,
    val totalPages: Int,
)
