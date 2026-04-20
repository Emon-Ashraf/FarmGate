package com.example.farmgate.presentation.farmer.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.data.model.OrderStatus
import com.example.farmgate.data.model.UnitType
import com.example.farmgate.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FarmerOrderDetailsViewModel(
    private val orderRepository: OrderRepository,
    private val orderId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(FarmerOrderDetailsUiState())
    val uiState: StateFlow<FarmerOrderDetailsUiState> = _uiState.asStateFlow()

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
                    val order = result.data
                    val initialFulfilledMap = order.items.associate { item ->
                        item.id to ((item.fulfilledQuantity ?: item.orderedQuantity).toString())
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        order = order,
                        fulfilledQuantities = initialFulfilledMap,
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

    fun acceptOrder() {
        val order = _uiState.value.order ?: return

        if (order.status != OrderStatus.Pending) {
            _uiState.value = _uiState.value.copy(
                actionErrorMessage = "Only pending orders can be accepted."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isAccepting = true,
                actionErrorMessage = null,
                actionSuccessMessage = null
            )

            when (val result = orderRepository.acceptOrder(orderId)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isAccepting = false,
                        order = result.data,
                        actionSuccessMessage = "Order accepted successfully."
                    )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isAccepting = false,
                        actionErrorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isAccepting = true)
                }
            }
        }
    }

    fun rejectOrder() {
        val order = _uiState.value.order ?: return

        if (order.status != OrderStatus.Pending) {
            _uiState.value = _uiState.value.copy(
                actionErrorMessage = "Only pending orders can be rejected."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isRejecting = true,
                actionErrorMessage = null,
                actionSuccessMessage = null
            )

            when (val result = orderRepository.rejectOrder(orderId)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isRejecting = false,
                        order = result.data,
                        actionSuccessMessage = "Order rejected successfully."
                    )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isRejecting = false,
                        actionErrorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isRejecting = true)
                }
            }
        }
    }

    fun onPickupCodeChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            pickupCode = value,
            actionErrorMessage = null,
            actionSuccessMessage = null
        )
    }

    fun onFulfilledQuantityChanged(orderItemId: Long, value: String) {
        _uiState.value = _uiState.value.copy(
            fulfilledQuantities = _uiState.value.fulfilledQuantities + (orderItemId to value),
            actionErrorMessage = null,
            actionSuccessMessage = null
        )
    }

    fun completeOrder() {
        val order = _uiState.value.order ?: return

        if (order.status != OrderStatus.Confirmed) {
            _uiState.value = _uiState.value.copy(
                actionErrorMessage = "Only confirmed orders can be completed."
            )
            return
        }

        val pickupCode = _uiState.value.pickupCode.trim()
        if (pickupCode.isBlank()) {
            _uiState.value = _uiState.value.copy(
                actionErrorMessage = "Enter pickup code."
            )
            return
        }

        val fulfilledPairs = mutableListOf<Pair<Long, Double>>()

        for (item in order.items) {
            val rawValue = _uiState.value.fulfilledQuantities[item.id]?.trim().orEmpty()
            val quantity = rawValue.toDoubleOrNull()

            if (quantity == null || quantity < 0.0) {
                _uiState.value = _uiState.value.copy(
                    actionErrorMessage = "Enter valid fulfilled quantity for all items."
                )
                return
            }

            if (quantity > item.orderedQuantity) {
                _uiState.value = _uiState.value.copy(
                    actionErrorMessage = "Fulfilled quantity cannot exceed ordered quantity."
                )
                return
            }

            val requiresInteger = item.unitType == UnitType.Piece ||
                    item.unitType == UnitType.Dozen ||
                    item.unitType == UnitType.Bundle

            if (requiresInteger && quantity != quantity.toInt().toDouble()) {
                _uiState.value = _uiState.value.copy(
                    actionErrorMessage = "Piece/Dozen/Bundle items require whole number fulfilled quantity."
                )
                return
            }

            fulfilledPairs.add(item.id to quantity)
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isCompleting = true,
                actionErrorMessage = null,
                actionSuccessMessage = null
            )

            when (
                val result = orderRepository.completeOrder(
                    orderId = orderId,
                    pickupCode = pickupCode,
                    fulfilledItems = fulfilledPairs
                )
            ) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isCompleting = false,
                        order = result.data,
                        actionSuccessMessage = "Order completed successfully."
                    )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isCompleting = false,
                        actionErrorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isCompleting = true)
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
            return FarmerOrderDetailsViewModel(
                orderRepository = orderRepository,
                orderId = orderId
            ) as T
        }
    }
}