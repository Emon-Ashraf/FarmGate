package com.example.farmgate.presentation.admin.product


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.data.repository.AdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminProductModerationViewModel(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminProductModerationUiState())
    val uiState: StateFlow<AdminProductModerationUiState> = _uiState.asStateFlow()

    fun deactivateProduct(productId: Long) {
        submitModeration(productId = productId, isActive = false)
    }

    fun activateProduct(productId: Long) {
        submitModeration(productId = productId, isActive = true)
    }

    private fun submitModeration(productId: Long, isActive: Boolean) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSubmitting = true,
                errorMessage = null,
                successMessage = null
            )

            when (val result = adminRepository.moderateProduct(productId, isActive)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        successMessage = if (isActive) {
                            "Product activated successfully."
                        } else {
                            "Product deactivated successfully."
                        },
                        targets = _uiState.value.targets.map {
                            if (it.productId == productId) it.copy(isActive = isActive) else it
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
            return AdminProductModerationViewModel(adminRepository) as T
        }
    }
}