package com.example.farmgate.data.remote.api


import com.example.farmgate.data.remote.dto.product.ProductDetailsDto
import com.example.farmgate.data.remote.dto.product.ProductListItemDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApi {

    @GET("api/products")
    suspend fun searchProducts(
        @Query("cityId") cityId: Long,
        @Query("query") query: String? = null,
        @Query("area") area: String? = null,
        @Query("category") category: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): Response<List<ProductListItemDto>>

    @GET("api/products/{id}")
    suspend fun getProductById(
        @Path("id") id: Long
    ): Response<ProductDetailsDto>

    @GET("api/products/me")
    suspend fun getMyProducts(): Response<List<ProductListItemDto>>
}