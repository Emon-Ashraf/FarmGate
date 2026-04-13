package com.example.farmgate.data.repository


import com.example.farmgate.core.common.Resource
import com.example.farmgate.core.network.safeApiCall
import com.example.farmgate.data.model.ProductDetails
import com.example.farmgate.data.model.ProductSummary
import com.example.farmgate.data.remote.api.ProductsApi
import com.example.farmgate.data.remote.mapper.toModel

class ProductRepository(
    private val productsApi: ProductsApi
) {

    suspend fun searchProducts(
        cityId: Long,
        query: String? = null,
        area: String? = null,
        category: String? = null,
        sortBy: String? = null,
        page: Int = 1,
        pageSize: Int = 20
    ): Resource<List<ProductSummary>> {
        return safeApiCall(
            apiCall = {
                productsApi.searchProducts(
                    cityId = cityId,
                    query = query,
                    area = area,
                    category = category,
                    sortBy = sortBy,
                    page = page,
                    pageSize = pageSize
                )
            },
            mapper = { dtoList -> dtoList.map { it.toModel() } }
        )
    }

    suspend fun getProductById(id: Long): Resource<ProductDetails> {
        return safeApiCall(
            apiCall = { productsApi.getProductById(id) },
            mapper = { dto -> dto.toModel() }
        )
    }

    suspend fun getMyProducts(): Resource<List<ProductSummary>> {
        return safeApiCall(
            apiCall = { productsApi.getMyProducts() },
            mapper = { dtoList -> dtoList.map { it.toModel() } }
        )
    }
}