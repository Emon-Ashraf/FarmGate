package com.example.farmgate.presentation.farmer.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FarmerOrdersViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FarmerOrdersUiState())
    val uiState: StateFlow<FarmerOrdersUiState> = _uiState.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            loadOrdersInternal(showFullLoading = true)
        }
    }

    fun refreshOrders() {
        viewModelScope.launch {
            loadOrdersInternal(showFullLoading = false)
        }
    }

    private suspend fun loadOrdersInternal(showFullLoading: Boolean) {
        if (showFullLoading) {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )
        }

        when (val result = orderRepository.getFarmerOrders()) {
            is Resource.Success -> {
                _uiState.value = FarmerOrdersUiState(
                    isLoading = false,
                    orders = result.data,
                    errorMessage = null
                )
            }

            is Resource.Error -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.message
                )
            }

            is Resource.Loading -> {
                if (showFullLoading) {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    class Factory(
        private val orderRepository: OrderRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FarmerOrdersViewModel(orderRepository) as T
        }
    }
}