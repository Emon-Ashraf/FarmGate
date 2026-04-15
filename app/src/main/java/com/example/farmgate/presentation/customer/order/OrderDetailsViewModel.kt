package com.example.farmgate.presentation.customer.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.data.model.OrderStatus
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
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        order = result.data,
                        errorMessage = null
                    )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
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

    fun onCancelNoteChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            cancelNote = value,
            actionErrorMessage = null,
            actionSuccessMessage = null
        )
    }

    fun onPaymentReferenceChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            paymentReference = value,
            actionErrorMessage = null,
            actionSuccessMessage = null
        )
    }

    fun cancelOrder() {
        val order = _uiState.value.order ?: return

        if (order.status != OrderStatus.Pending && order.status != OrderStatus.AwaitingFee) {
            _uiState.value = _uiState.value.copy(
                actionErrorMessage = "This order cannot be cancelled from its current state."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isCancelling = true,
                actionErrorMessage = null,
                actionSuccessMessage = null
            )

            when (
                val result = orderRepository.cancelOrder(
                    orderId = orderId,
                    note = _uiState.value.cancelNote.takeIf { it.isNotBlank() }
                )
            ) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isCancelling = false,
                        order = result.data,
                        actionSuccessMessage = "Order cancelled successfully."
                    )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isCancelling = false,
                        actionErrorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(
                        isCancelling = true
                    )
                }
            }
        }
    }

    fun confirmServiceFee() {
        val order = _uiState.value.order ?: return

        if (order.status != OrderStatus.AwaitingFee) {
            _uiState.value = _uiState.value.copy(
                actionErrorMessage = "Service fee can only be confirmed when the order is AwaitingFee."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isConfirmingFee = true,
                actionErrorMessage = null,
                actionSuccessMessage = null
            )

            when (
                val result = orderRepository.confirmServiceFee(
                    orderId = orderId,
                    paymentReference = _uiState.value.paymentReference.takeIf { it.isNotBlank() }
                )
            ) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isConfirmingFee = false,
                        order = result.data,
                        actionSuccessMessage = "Service fee confirmed successfully."
                    )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isConfirmingFee = false,
                        actionErrorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(
                        isConfirmingFee = true
                    )
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