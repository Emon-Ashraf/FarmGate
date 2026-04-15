package com.example.farmgate.presentation.customer.productdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.core.navigation.Routes
import com.example.farmgate.data.model.UnitType
import com.example.farmgate.data.repository.DraftAddResult
import com.example.farmgate.data.repository.OrderDraftRepository
import com.example.farmgate.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val productRepository: ProductRepository,
    private val orderDraftRepository: OrderDraftRepository,
    private val productId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    private val _navigation = MutableSharedFlow<String>()
    val navigation: SharedFlow<String> = _navigation.asSharedFlow()

    init {
        loadProduct()

        viewModelScope.launch {
            orderDraftRepository.draft.collect { draft ->
                _uiState.value = _uiState.value.copy(
                    hasActiveDraft = draft != null
                )
            }
        }
    }

    fun loadProduct() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            when (val result = productRepository.getProductById(productId)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        product = result.data,
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
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun onOrderQuantityChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            orderQuantity = value,
            orderErrorMessage = null,
            infoMessage = null
        )
    }

    fun addToOrder() {
        val product = _uiState.value.product ?: return
        val quantityText = _uiState.value.orderQuantity.trim()

        if (quantityText.isBlank()) {
            _uiState.value = _uiState.value.copy(
                orderErrorMessage = "Enter quantity."
            )
            return
        }

        val quantity = quantityText.toDoubleOrNull()
        if (quantity == null || quantity <= 0.0) {
            _uiState.value = _uiState.value.copy(
                orderErrorMessage = "Enter a valid quantity."
            )
            return
        }

        val requiresInteger = product.unitType == UnitType.Piece ||
                product.unitType == UnitType.Dozen ||
                product.unitType == UnitType.Bundle

        if (requiresInteger && quantity != quantity.toInt().toDouble()) {
            _uiState.value = _uiState.value.copy(
                orderErrorMessage = "This product requires a whole number quantity."
            )
            return
        }

        when (orderDraftRepository.addProduct(product, quantity)) {
            DraftAddResult.Added -> {
                _uiState.value = _uiState.value.copy(
                    infoMessage = "Added to your order draft.",
                    orderErrorMessage = null
                )
            }

            DraftAddResult.Conflict -> {
                _uiState.value = _uiState.value.copy(
                    orderErrorMessage = "Current draft contains items from another pickup location. Clear the draft in Review Order first.",
                    infoMessage = null
                )
            }

            DraftAddResult.InvalidQuantity -> {
                _uiState.value = _uiState.value.copy(
                    orderErrorMessage = "Invalid quantity.",
                    infoMessage = null
                )
            }
        }
    }

    fun openReviewOrder() {
        viewModelScope.launch {
            _navigation.emit(Routes.CUSTOMER_REVIEW_ORDER)
        }
    }

    class Factory(
        private val productRepository: ProductRepository,
        private val orderDraftRepository: OrderDraftRepository,
        private val productId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProductDetailsViewModel(
                productRepository = productRepository,
                orderDraftRepository = orderDraftRepository,
                productId = productId
            ) as T
        }
    }
}