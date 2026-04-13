package com.example.farmgate.data.remote.dto.product


data class ProductSearchRequestDto(
    val cityId: Long,
    val query: String? = null,
    val area: String? = null,
    val category: String? = null,
    val sortBy: String? = null,
    val page: Int = 1,
    val pageSize: Int = 20
)