package com.example.farmgate.data.remote.dto.pickuplocation


data class CreatePickupLocationDto(
    val cityId: Long,
    val areaName: String,
    val addressLine: String,
    val latitude: Double,
    val longitude: Double,
    val instructions: String?
)