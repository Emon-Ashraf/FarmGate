package com.example.farmgate.presentation.farmer.product


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FarmerProductsViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FarmerProductsUiState())
    val uiState: StateFlow<FarmerProductsUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            when (val result = productRepository.getMyProducts()) {
                is Resource.Success -> {
                    _uiState.value = FarmerProductsUiState(
                        isLoading = false,
                        products = result.data,
                        errorMessage = null
                    )
                }

                is Resource.Error -> {
                    _uiState.value = FarmerProductsUiState(
                        isLoading = false,
                        products = emptyList(),
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
        private val productRepository: ProductRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FarmerProductsViewModel(productRepository) as T
        }
    }
}