package com.example.farmgate.presentation.customer.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.data.repository.CityRepository
import com.example.farmgate.data.repository.ProductRepository
import com.example.farmgate.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CustomerHomeViewModel(
    private val profileRepository: ProfileRepository,
    private val cityRepository: CityRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerHomeUiState(isLoading = true))
    val uiState: StateFlow<CustomerHomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            val profileResult = profileRepository.getMyProfile()
            val citiesResult = cityRepository.getCities()

            if (profileResult is Resource.Error) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = profileResult.message
                )
                return@launch
            }

            if (citiesResult is Resource.Error) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = citiesResult.message
                )
                return@launch
            }

            val profile = (profileResult as Resource.Success).data
            val cities = (citiesResult as Resource.Success).data

            val initialSelectedCityId = profile.primaryCityId ?: cities.firstOrNull()?.id
            val initialSelectedCityName = cities.firstOrNull {
                it.id == initialSelectedCityId
            }?.name

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                fullName = profile.fullName,
                cities = cities,
                selectedCityId = initialSelectedCityId,
                selectedCityName = initialSelectedCityName,
                errorMessage = null
            )

            if (initialSelectedCityId != null) {
                loadProductsForCity(initialSelectedCityId)
            }
        }
    }

    fun onRetry() {
        loadHomeData()
    }

    fun onCitySelected(cityId: Long) {
        val selectedCity = _uiState.value.cities.firstOrNull { it.id == cityId }

        _uiState.value = _uiState.value.copy(
            selectedCityId = cityId,
            selectedCityName = selectedCity?.name,
            products = emptyList(),
            errorMessage = null
        )

        loadProductsForCity(cityId)
    }

    private fun loadProductsForCity(cityId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isProductsLoading = true,
                errorMessage = null
            )

            when (val result = productRepository.searchProducts(cityId = cityId)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isProductsLoading = false,
                        products = result.data,
                        errorMessage = null
                    )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isProductsLoading = false,
                        errorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(
                        isProductsLoading = true,
                        errorMessage = null
                    )
                }
            }
        }
    }

    class Factory(
        private val profileRepository: ProfileRepository,
        private val cityRepository: CityRepository,
        private val productRepository: ProductRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CustomerHomeViewModel(
                profileRepository = profileRepository,
                cityRepository = cityRepository,
                productRepository = productRepository
            ) as T
        }
    }
}