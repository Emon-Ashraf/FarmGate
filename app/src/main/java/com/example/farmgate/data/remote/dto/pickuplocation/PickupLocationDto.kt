package com.example.farmgate.data.remote.dto.pickuplocation


data class PickupLocationDto(
    val id: Long,
    val cityId: Long,
    val cityName: String,
    val areaName: String,
    val addressLine: String,
    val latitude: Double,
    val longitude: Double,
    val instructions: String?,
    val isActive: Boolean
)