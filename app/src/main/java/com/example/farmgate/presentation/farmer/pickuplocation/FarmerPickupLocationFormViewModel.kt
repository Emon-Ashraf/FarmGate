package com.example.farmgate.presentation.farmer.pickuplocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.data.repository.CityRepository
import com.example.farmgate.data.repository.PickupLocationRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FarmerPickupLocationFormViewModel(
    private val pickupLocationRepository: PickupLocationRepository,
    private val cityRepository: CityRepository,
    private val pickupLocationId: Long?
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        FarmerPickupLocationFormUiState(
            isLoading = true,
            isEditMode = pickupLocationId != null
        )
    )
    val uiState: StateFlow<FarmerPickupLocationFormUiState> = _uiState.asStateFlow()

    private val _navigation = MutableSharedFlow<Unit>()
    val navigation: SharedFlow<Unit> = _navigation.asSharedFlow()

    init {
        loadInitialData()
    }

    fun onCityChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            selectedCityId = value,
            errorMessage = null
        )
    }

    fun onAreaNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            areaName = value,
            errorMessage = null
        )
    }

    fun onAddressLineChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            addressLine = value,
            errorMessage = null
        )
    }

    fun onInstructionsChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            instructions = value,
            errorMessage = null
        )
    }

    fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            val citiesResult = cityRepository.getCities()
            val locationsResult = pickupLocationRepository.getMyPickupLocations()

            when (citiesResult) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(cities = citiesResult.data)
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = citiesResult.message
                    )
                    return@launch
                }

                is Resource.Loading -> {}
            }

            if (pickupLocationId == null) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                return@launch
            }

            when (locationsResult) {
                is Resource.Success -> {
                    val location = locationsResult.data.firstOrNull { it.id == pickupLocationId }

                    if (location == null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Pickup location not found."
                        )
                        return@launch
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        selectedCityId = location.cityId.toString(),
                        areaName = location.areaName,
                        addressLine = location.addressLine,
                        instructions = location.instructions.orEmpty()
                    )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = locationsResult.message
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun savePickupLocation() {
        val state = _uiState.value

        val cityId = state.selectedCityId.toLongOrNull()
        if (cityId == null || cityId <= 0L) {
            _uiState.value = state.copy(errorMessage = "Please select a city.")
            return
        }

        if (state.areaName.trim().isBlank()) {
            _uiState.value = state.copy(errorMessage = "Please enter area name.")
            return
        }

        if (state.addressLine.trim().isBlank()) {
            _uiState.value = state.copy(errorMessage = "Please enter address line.")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(
                isSaving = true,
                errorMessage = null,
                successMessage = null
            )

            val result = if (pickupLocationId == null) {
                pickupLocationRepository.createPickupLocation(
                    cityId = cityId,
                    areaName = state.areaName.trim(),
                    addressLine = state.addressLine.trim(),
                    latitude = null,
                    longitude = null,
                    instructions = state.instructions.trim().ifBlank { null }
                )
            } else {
                pickupLocationRepository.updatePickupLocation(
                    id = pickupLocationId,
                    cityId = cityId,
                    areaName = state.areaName.trim(),
                    addressLine = state.addressLine.trim(),
                    latitude = null,
                    longitude = null,
                    instructions = state.instructions.trim().ifBlank { null }
                )
            }

            when (result) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        successMessage = if (pickupLocationId == null) {
                            "Pickup location created successfully."
                        } else {
                            "Pickup location updated successfully."
                        }
                    )
                    _navigation.emit(Unit)
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        errorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isSaving = true)
                }
            }
        }
    }

    fun deactivatePickupLocation() {
        val id = pickupLocationId ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDeleting = true,
                errorMessage = null,
                successMessage = null
            )

            when (val result = pickupLocationRepository.deactivatePickupLocation(id)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        successMessage = "Pickup location deactivated successfully."
                    )
                    _navigation.emit(Unit)
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        errorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isDeleting = true)
                }
            }
        }
    }

    class Factory(
        private val pickupLocationRepository: PickupLocationRepository,
        private val cityRepository: CityRepository,
        private val pickupLocationId: Long?
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FarmerPickupLocationFormViewModel(
                pickupLocationRepository = pickupLocationRepository,
                cityRepository = cityRepository,
                pickupLocationId = pickupLocationId
            ) as T
        }
    }
}