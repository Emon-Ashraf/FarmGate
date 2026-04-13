package com.example.farmgate.data.remote.mapper


import com.example.farmgate.data.model.City
import com.example.farmgate.data.remote.dto.city.CityResponseDto

fun CityResponseDto.toModel(): City {
    return City(
        id = id,
        name = name
    )
}