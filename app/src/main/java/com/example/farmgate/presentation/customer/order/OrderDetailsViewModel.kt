package com.example.farmgate.presentation.customer.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderDetailsViewModel(
    private val orderRepository: OrderRepository,
    private val orderId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderDetailsUiState())
    val uiState: StateFlow<OrderDetailsUiState> = _uiState.asStateFlow()

    init {
        loadOrder()
    }

    fun loadOrder() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            when (val result = orderRepository.getOrderDetails(orderId)) {
                is Resource.Success -> {
                    _uiState.value = OrderDetailsUiState(
                        isLoading = false,
                        order = result.data,
                        errorMessage = null
                    )
                }

                is Resource.Error -> {
                    _uiState.value = OrderDetailsUiState(
                        isLoading = false,
                        order = null,
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
        private val orderRepository: OrderRepository,
        private val orderId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return OrderDetailsViewModel(
                orderRepository = orderRepository,
                orderId = orderId
            ) as T
        }
    }
}