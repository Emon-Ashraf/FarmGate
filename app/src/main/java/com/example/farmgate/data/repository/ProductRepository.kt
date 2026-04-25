package com.example.farmgate.data.repository

import com.example.farmgate.core.common.Resource
import com.example.farmgate.core.network.safeApiCall
import com.example.farmgate.data.model.ProductDetails
import com.example.farmgate.data.model.ProductSummary
import com.example.farmgate.data.remote.api.ProductsApi
import com.example.farmgate.data.remote.dto.product.CreateProductRequestDto
import com.example.farmgate.data.remote.dto.product.UpdateProductRequestDto
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

    suspend fun createProduct(
        pickupLocationId: Long,
        name: String,
        description: String?,
        category: String?,
        unitType: Int,
        pricePerUnit: Double,
        availableQuantity: Double,
        imageUrl: String?
    ): Resource<ProductDetails> {
        return safeApiCall(
            apiCall = {
                productsApi.createProduct(
                    CreateProductRequestDto(
                        pickupLocationId = pickupLocationId,
                        name = name,
                        description = description,
                        category = category,
                        unitType = unitType,
                        pricePerUnit = pricePerUnit,
                        availableQuantity = availableQuantity,
                        imageUrl = imageUrl
                    )
                )
            },
            mapper = { dto -> dto.toModel() }
        )
    }

    suspend fun updateProduct(
        id: Long,
        pickupLocationId: Long,
        name: String,
        description: String?,
        category: String?,
        unitType: Int,
        pricePerUnit: Double,
        availableQuantity: Double,
        imageUrl: String?
    ): Resource<ProductDetails> {
        return safeApiCall(
            apiCall = {
                productsApi.updateProduct(
                    id = id,
                    request = UpdateProductRequestDto(
                        pickupLocationId = pickupLocationId,
                        name = name,
                        description = description,
                        category = category,
                        unitType = unitType,
                        pricePerUnit = pricePerUnit,
                        availableQuantity = availableQuantity,
                        imageUrl = imageUrl
                    )
                )
            },
            mapper = { dto -> dto.toModel() }
        )
    }

    suspend fun deactivateProduct(id: Long): Resource<Unit> {
        return safeApiCall(
            apiCall = { productsApi.deactivateProduct(id) },
            mapper = { Unit }
        )
    }
}