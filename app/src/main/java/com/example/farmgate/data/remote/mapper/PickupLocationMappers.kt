package com.example.farmgate.data.remote.mapper


import com.example.farmgate.data.model.PickupLocation
import com.example.farmgate.data.remote.dto.pickuplocation.PickupLocationDto

fun PickupLocationDto.toModel(): PickupLocation {
    return PickupLocation(
        id = id,
        cityId = cityId,
        cityName = cityName,
        areaName = areaName,
        addressLine = addressLine,
        latitude = latitude,
        longitude = longitude,
        instructions = instructions,
        isActive = isActive
    )
}