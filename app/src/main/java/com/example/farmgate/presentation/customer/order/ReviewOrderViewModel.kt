package com.example.farmgate.presentation.customer.order


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.core.navigation.Routes
import com.example.farmgate.data.repository.OrderDraftRepository
import com.example.farmgate.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReviewOrderViewModel(
    private val orderDraftRepository: OrderDraftRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewOrderUiState())
    val uiState: StateFlow<ReviewOrderUiState> = _uiState.asStateFlow()

    private val _navigation = MutableSharedFlow<String>()
    val navigation: SharedFlow<String> = _navigation.asSharedFlow()

    init {
        viewModelScope.launch {
            orderDraftRepository.draft.collect { draft ->
                _uiState.value = _uiState.value.copy(draft = draft)
            }
        }
    }

    fun updateQuantity(productId: Long, quantityText: String) {
        val quantity = quantityText.toDoubleOrNull() ?: return
        orderDraftRepository.updateItemQuantity(productId, quantity)
    }

    fun removeItem(productId: Long) {
        orderDraftRepository.removeItem(productId)
    }

    fun clearDraft() {
        orderDraftRepository.clearDraft()
    }

    fun submitOrder() {
        val draft = _uiState.value.draft
        if (draft == null || draft.items.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Your order draft is empty."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSubmitting = true,
                errorMessage = null
            )

            when (val result = orderRepository.createOrder(draft)) {
                is Resource.Success -> {
                    orderDraftRepository.clearDraft()
                    _uiState.value = _uiState.value.copy(isSubmitting = false)
                    _navigation.emit(Routes.customerOrderDetails(result.data.id))
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
        private val orderDraftRepository: OrderDraftRepository,
        private val orderRepository: OrderRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ReviewOrderViewModel(
                orderDraftRepository = orderDraftRepository,
                orderRepository = orderRepository
            ) as T
        }
    }
}