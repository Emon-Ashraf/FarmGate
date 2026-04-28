package com.example.farmgate.presentation.farmer.profile


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.core.navigation.Graph
import com.example.farmgate.data.repository.AuthRepository
import com.example.farmgate.data.repository.CityRepository
import com.example.farmgate.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FarmerProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val cityRepository: CityRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FarmerProfileUiState())
    val uiState: StateFlow<FarmerProfileUiState> = _uiState.asStateFlow()

    private val _navigation = MutableSharedFlow<String>()
    val navigation: SharedFlow<String> = _navigation.asSharedFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
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

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                profile = profile,
                cities = cities,
                displayName = profile.displayName.orEmpty(),
                description = profile.description.orEmpty(),
                selectedCityId = profile.primaryCityId,
                selectedCityName = profile.primaryCityName,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun onDisplayNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            displayName = value,
            errorMessage = null,
            successMessage = null
        )
    }

    fun onDescriptionChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            description = value,
            errorMessage = null,
            successMessage = null
        )
    }

    fun onCitySelected(cityId: Long) {
        val city = _uiState.value.cities.firstOrNull { it.id == cityId }

        _uiState.value = _uiState.value.copy(
            selectedCityId = cityId,
            selectedCityName = city?.name,
            errorMessage = null,
            successMessage = null
        )
    }

    fun startEdit() {
        _uiState.value = _uiState.value.copy(
            isEditMode = true,
            errorMessage = null,
            successMessage = null
        )
    }

    fun cancelEdit() {
        val profile = _uiState.value.profile ?: return

        _uiState.value = _uiState.value.copy(
            isEditMode = false,
            displayName = profile.displayName.orEmpty(),
            description = profile.description.orEmpty(),
            selectedCityId = profile.primaryCityId,
            selectedCityName = profile.primaryCityName,
            errorMessage = null,
            successMessage = null
        )
    }

    fun saveProfile() {
        val state = _uiState.value

        if (state.displayName.isBlank()) {
            _uiState.value = state.copy(
                errorMessage = "Enter display name."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(
                isSaving = true,
                errorMessage = null,
                successMessage = null
            )

            when (
                val result = profileRepository.updateFarmerProfile(
                    displayName = state.displayName.trim(),
                    description = state.description.trim().ifBlank { null },
                    primaryCityId = state.selectedCityId
                )
            ) {
                is Resource.Success -> {
                    val updated = result.data
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        isEditMode = false,
                        profile = updated,
                        displayName = updated.displayName.orEmpty(),
                        description = updated.description.orEmpty(),
                        selectedCityId = updated.primaryCityId,
                        selectedCityName = updated.primaryCityName,
                        errorMessage = null,
                        successMessage = "Profile updated successfully."
                    )
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

    fun logout() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoggingOut = true)
            authRepository.logout()
            _uiState.value = _uiState.value.copy(isLoggingOut = false)
            _navigation.emit(Graph.AUTH)
        }
    }

    class Factory(
        private val profileRepository: ProfileRepository,
        private val cityRepository: CityRepository,
        private val authRepository: AuthRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FarmerProfileViewModel(
                profileRepository = profileRepository,
                cityRepository = cityRepository,
                authRepository = authRepository
            ) as T
        }
    }
}