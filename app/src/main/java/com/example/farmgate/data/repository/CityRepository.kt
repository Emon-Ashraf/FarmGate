package com.example.farmgate.data.repository

import com.example.farmgate.core.common.Resource
import com.example.farmgate.core.network.safeApiCall
import com.example.farmgate.data.model.City
import com.example.farmgate.data.remote.api.CitiesApi
import com.example.farmgate.data.remote.mapper.toModel

class CityRepository(
    private val citiesApi: CitiesApi
) {
    suspend fun getCities(): Resource<List<City>> {
        return safeApiCall(
            apiCall = { citiesApi.getCities() },
            mapper = { dtoList -> dtoList.map { it.toModel() } }
        )
    }
}