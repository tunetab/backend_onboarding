package com.icerockdev.application.tests.common.api.client.pagination

data class Pagination(
    val page: Int = 0,
    val size: Int = 20,
    val sort: String = "createdAt,ASC"
) {
    fun toMap(): MutableMap<String, List<String>> {
        return mutableMapOf(
            "page" to listOf(page.toString()),
            "size" to listOf(size.toString()),
            "sort" to listOf(sort)
        )
    }
}
