package com.example.farmgate.data.repository


import com.example.farmgate.core.common.Resource
import com.example.farmgate.core.network.safeApiCall
import com.example.farmgate.data.model.PickupLocation
import com.example.farmgate.data.remote.api.PickupLocationsApi
import com.example.farmgate.data.remote.mapper.toModel

class PickupLocationRepository(
    private val pickupLocationsApi: PickupLocationsApi
) {
    suspend fun getMyPickupLocations(): Resource<List<PickupLocation>> {
        return safeApiCall(
            apiCall = { pickupLocationsApi.getMyPickupLocations() },
            mapper = { dtoList -> dtoList.map { it.toModel() } }
        )
    }
}