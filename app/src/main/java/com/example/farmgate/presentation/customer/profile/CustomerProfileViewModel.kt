package com.example.farmgate.presentation.customer.profile

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

class CustomerProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val cityRepository: CityRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerProfileUiState())
    val uiState: StateFlow<CustomerProfileUiState> = _uiState.asStateFlow()

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
                actionErrorMessage = null,
                actionSuccessMessage = null
            )

            val profileResult = profileRepository.getMyProfile()
            val citiesResult = cityRepository.getCities()

            if (profileResult is Resource.Success && citiesResult is Resource.Success) {
                val profile = profileResult.data

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    profile = profile,
                    cities = citiesResult.data,
                    selectedCityId = profile.primaryCityId,
                    selectedCityName = profile.primaryCityName,
                    errorMessage = null
                )
                return@launch
            }

            val message = when {
                profileResult is Resource.Error -> profileResult.message
                citiesResult is Resource.Error -> citiesResult.message
                else -> "Failed to load profile."
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                profile = null,
                errorMessage = message
            )
        }
    }

    fun loadProfile() {
        loadData()
    }

    fun enterEditMode() {
        val profile = _uiState.value.profile ?: return

        _uiState.value = _uiState.value.copy(
            isEditMode = true,
            selectedCityId = profile.primaryCityId,
            selectedCityName = profile.primaryCityName,
            actionErrorMessage = null,
            actionSuccessMessage = null
        )
    }

    fun cancelEditMode() {
        val profile = _uiState.value.profile

        _uiState.value = _uiState.value.copy(
            isEditMode = false,
            selectedCityId = profile?.primaryCityId,
            selectedCityName = profile?.primaryCityName,
            actionErrorMessage = null,
            actionSuccessMessage = null
        )
    }

    fun onCitySelected(cityId: Long) {
        val cityName = _uiState.value.cities.firstOrNull { it.id == cityId }?.name

        _uiState.value = _uiState.value.copy(
            selectedCityId = cityId,
            selectedCityName = cityName,
            actionErrorMessage = null,
            actionSuccessMessage = null
        )
    }

    fun saveProfile() {
        val selectedCityId = _uiState.value.selectedCityId

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                actionErrorMessage = null,
                actionSuccessMessage = null
            )

            when (
                val result = profileRepository.updateCustomerProfile(
                    primaryCityId = selectedCityId
                )
            ) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        isEditMode = false,
                        profile = result.data,
                        selectedCityId = result.data.primaryCityId,
                        selectedCityName = result.data.primaryCityName,
                        actionSuccessMessage = "Profile updated successfully.",
                        actionErrorMessage = null
                    )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        actionErrorMessage = result.message,
                        actionSuccessMessage = null
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
            return CustomerProfileViewModel(
                profileRepository = profileRepository,
                cityRepository = cityRepository,
                authRepository = authRepository
            ) as T
        }
    }
}