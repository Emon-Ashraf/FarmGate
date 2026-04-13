package com.example.farmgate.data.remote.mapper


import com.example.farmgate.data.model.ProductDetails
import com.example.farmgate.data.model.ProductSummary
import com.example.farmgate.data.model.UnitType
import com.example.farmgate.data.remote.dto.product.ProductDetailsDto
import com.example.farmgate.data.remote.dto.product.ProductListItemDto

fun ProductListItemDto.toModel(): ProductSummary {
    return ProductSummary(
        id = id,
        name = name,
        unitType = UnitType.fromInt(unitType),
        pricePerUnit = pricePerUnit,
        availableQuantity = availableQuantity,
        pickupLocationId = pickupLocationId,
        pickupArea = pickupArea,
        cityName = cityName,
        farmerName = farmerName,
        imageUrl = imageUrl
    )
}

fun ProductDetailsDto.toModel(): ProductDetails {
    return ProductDetails(
        id = id,
        name = name,
        description = description,
        category = category,
        unitType = UnitType.fromInt(unitType),
        pricePerUnit = pricePerUnit,
        availableQuantity = availableQuantity,
        pickupLocationId = pickupLocationId,
        pickupArea = pickupArea,
        cityName = cityName,
        pickupAddress = pickupAddress,
        instructions = instructions,
        farmerName = farmerName,
        farmerPhone = farmerPhone,
        imageUrl = imageUrl
    )
}