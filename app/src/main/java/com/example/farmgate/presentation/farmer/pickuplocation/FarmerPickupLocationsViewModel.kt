package com.example.farmgate.presentation.farmer.pickuplocation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.data.repository.PickupLocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FarmerPickupLocationsViewModel(
    private val pickupLocationRepository: PickupLocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FarmerPickupLocationsUiState())
    val uiState: StateFlow<FarmerPickupLocationsUiState> = _uiState.asStateFlow()

    init {
        loadPickupLocations()
    }

    fun loadPickupLocations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            when (val result = pickupLocationRepository.getMyPickupLocations()) {
                is Resource.Success -> {
                    _uiState.value = FarmerPickupLocationsUiState(
                        isLoading = false,
                        locations = result.data,
                        errorMessage = null
                    )
                }

                is Resource.Error -> {
                    _uiState.value = FarmerPickupLocationsUiState(
                        isLoading = false,
                        locations = emptyList(),
                        errorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    class Factory(
        private val pickupLocationRepository: PickupLocationRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FarmerPickupLocationsViewModel(pickupLocationRepository) as T
        }
    }
}