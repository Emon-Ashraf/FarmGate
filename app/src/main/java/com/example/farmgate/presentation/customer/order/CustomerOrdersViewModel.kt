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

class CustomerOrdersViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerOrdersUiState())
    val uiState: StateFlow<CustomerOrdersUiState> = _uiState.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            when (val result = orderRepository.getCustomerOrders()) {
                is Resource.Success -> {
                    _uiState.value = CustomerOrdersUiState(
                        isLoading = false,
                        orders = result.data,
                        errorMessage = null
                    )
                }

                is Resource.Error -> {
                    _uiState.value = CustomerOrdersUiState(
                        isLoading = false,
                        orders = emptyList(),
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
        private val orderRepository: OrderRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CustomerOrdersViewModel(orderRepository) as T
        }
    }
}