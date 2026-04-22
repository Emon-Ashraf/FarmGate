package com.example.farmgate.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.core.navigation.Routes
import com.example.farmgate.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _navigation = MutableSharedFlow<String>()
    val navigation: SharedFlow<String> = _navigation.asSharedFlow()

    fun onFullNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(fullName = value, errorMessage = null)
    }

    fun onPhoneNumberChanged(value: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = value, errorMessage = null)
    }

    fun onEmailChanged(value: String) {
        _uiState.value = _uiState.value.copy(email = value, errorMessage = null)
    }

    fun onPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(password = value, errorMessage = null)
    }

    fun onRoleChanged(value: Int) {
        _uiState.value = _uiState.value.copy(
            role = value,
            errorMessage = null,
            displayName = if (value == 2) _uiState.value.displayName else ""
        )
    }

    fun onDisplayNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(displayName = value, errorMessage = null)
    }

    fun register() {
        val state = _uiState.value

        if (state.fullName.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Enter full name.")
            return
        }

        if (state.phoneNumber.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Enter phone number.")
            return
        }

        if (state.password.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Enter password.")
            return
        }

        if (state.role == 2 && state.displayName.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Enter display name for farmer.")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, errorMessage = null)

            when (
                val result = authRepository.register(
                    fullName = state.fullName.trim(),
                    phoneNumber = state.phoneNumber.trim(),
                    email = state.email.trim().ifBlank { null },
                    password = state.password,
                    role = state.role,
                    primaryCityId = state.primaryCityId,
                    displayName = state.displayName.trim().ifBlank { null }
                )
            ) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _navigation.emit(Routes.graphForRole(result.data.role))
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
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
        private val authRepository: AuthRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RegisterViewModel(authRepository = authRepository) as T
        }
    }
}