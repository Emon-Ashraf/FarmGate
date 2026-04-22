package com.example.farmgate.presentation.customer.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.data.repository.CityRepository
import com.example.farmgate.data.repository.OrderDraftRepository
import com.example.farmgate.data.repository.ProductRepository
import com.example.farmgate.data.repository.ProfileRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CustomerHomeViewModel(
    private val profileRepository: ProfileRepository,
    private val cityRepository: CityRepository,
    private val productRepository: ProductRepository,
    private val orderDraftRepository: OrderDraftRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerHomeUiState(isLoading = true))
    val uiState: StateFlow<CustomerHomeUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadHomeData()

        viewModelScope.launch {
            orderDraftRepository.draft.collect { draft ->
                _uiState.value = _uiState.value.copy(
                    hasActiveDraft = draft != null
                )
            }
        }
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                screenErrorMessage = null,
                productsErrorMessage = null
            )

            val profileResult = profileRepository.getMyProfile()
            val citiesResult = cityRepository.getCities()

            if (profileResult is Resource.Error) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    screenErrorMessage = profileResult.message
                )
                return@launch
            }

            if (citiesResult is Resource.Error) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    screenErrorMessage = citiesResult.message
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
                screenErrorMessage = null,
                productsErrorMessage = null
            )

            if (initialSelectedCityId != null) {
                loadProductsForCity(initialSelectedCityId)
            }
        }
    }

    fun onRetry() {
        val currentState = _uiState.value

        if (currentState.cities.isEmpty() || currentState.selectedCityId == null) {
            loadHomeData()
        } else {
            loadProductsForCity(currentState.selectedCityId)
        }
    }

    fun onCitySelected(cityId: Long) {
        val selectedCity = _uiState.value.cities.firstOrNull { it.id == cityId }

        _uiState.value = _uiState.value.copy(
            selectedCityId = cityId,
            selectedCityName = selectedCity?.name,
            products = emptyList(),
            productsErrorMessage = null
        )

        loadProductsForCity(cityId)
    }

    fun onSearchQueryChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = value,
            productsErrorMessage = null
        )

        val cityId = _uiState.value.selectedCityId ?: return

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(350)
            loadProductsForCity(cityId)
        }
    }

    private fun loadProductsForCity(cityId: Long) {
        viewModelScope.launch {
            val query = _uiState.value.searchQuery.trim().ifBlank { null }

            _uiState.value = _uiState.value.copy(
                isProductsLoading = true,
                productsErrorMessage = null
            )

            when (
                val result = productRepository.searchProducts(
                    cityId = cityId,
                    query = query
                )
            ) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isProductsLoading = false,
                        products = result.data,
                        productsErrorMessage = null
                    )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isProductsLoading = false,
                        productsErrorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(
                        isProductsLoading = true,
                        productsErrorMessage = null
                    )
                }
            }
        }
    }

    class Factory(
        private val profileRepository: ProfileRepository,
        private val cityRepository: CityRepository,
        private val productRepository: ProductRepository,
        private val orderDraftRepository: OrderDraftRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CustomerHomeViewModel(
                profileRepository = profileRepository,
                cityRepository = cityRepository,
                productRepository = productRepository,
                orderDraftRepository = orderDraftRepository
            ) as T
        }
    }
}