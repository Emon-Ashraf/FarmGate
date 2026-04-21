package com.example.farmgate.presentation.admin.user


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.data.repository.AdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminUserModerationViewModel(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUserModerationUiState())
    val uiState: StateFlow<AdminUserModerationUiState> = _uiState.asStateFlow()

    fun deactivateUser(userId: Long) {
        submitModeration(userId = userId, isActive = false)
    }

    fun activateUser(userId: Long) {
        submitModeration(userId = userId, isActive = true)
    }

    private fun submitModeration(userId: Long, isActive: Boolean) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSubmitting = true,
                errorMessage = null,
                successMessage = null
            )

            when (val result = adminRepository.moderateUser(userId, isActive)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        successMessage = if (isActive) {
                            "User activated successfully."
                        } else {
                            "User deactivated successfully."
                        },
                        targets = _uiState.value.targets.map {
                            if (it.userId == userId) it.copy(isActive = isActive) else it
                        }
                    )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        errorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isSubmitting = true)
                }
            }
        }
    }

    class Factory(
        private val adminRepository: AdminRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AdminUserModerationViewModel(adminRepository) as T
        }
    }
}