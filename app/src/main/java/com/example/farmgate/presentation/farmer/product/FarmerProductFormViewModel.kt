package com.example.farmgate.presentation.farmer.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.data.repository.PickupLocationRepository
import com.example.farmgate.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FarmerProductFormViewModel(
    private val productRepository: ProductRepository,
    private val pickupLocationRepository: PickupLocationRepository,
    private val productId: Long?
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        FarmerProductFormUiState(
            isLoading = true,
            isEditMode = productId != null
        )
    )
    val uiState: StateFlow<FarmerProductFormUiState> = _uiState.asStateFlow()

    private val _navigation = MutableSharedFlow<Unit>()
    val navigation: SharedFlow<Unit> = _navigation.asSharedFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            when (val pickupResult = pickupLocationRepository.getMyPickupLocations()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        pickupLocations = pickupResult.data
                    )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = pickupResult.message
                    )
                    return@launch
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }

            if (productId != null) {
                loadProduct()
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun onPickupLocationIdChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            pickupLocationId = value,
            errorMessage = null
        )
    }

    fun onNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(name = value, errorMessage = null)
    }

    fun onDescriptionChanged(value: String) {
        _uiState.value = _uiState.value.copy(description = value, errorMessage = null)
    }

    fun onCategoryChanged(value: String) {
        _uiState.value = _uiState.value.copy(category = value, errorMessage = null)
    }

    fun onUnitTypeChanged(value: Int) {
        _uiState.value = _uiState.value.copy(unitType = value, errorMessage = null)
    }

    fun onPricePerUnitChanged(value: String) {
        _uiState.value = _uiState.value.copy(pricePerUnit = value, errorMessage = null)
    }

    fun onAvailableQuantityChanged(value: String) {
        _uiState.value = _uiState.value.copy(availableQuantity = value, errorMessage = null)
    }

    fun onImageUrlChanged(value: String) {
        _uiState.value = _uiState.value.copy(imageUrl = value, errorMessage = null)
    }

    fun loadProduct() {
        val id = productId ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            when (val result = productRepository.getProductById(id)) {
                is Resource.Success -> {
                    val product = result.data
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        pickupLocationId = product.pickupLocationId.toString(),
                        name = product.name,
                        description = product.description.orEmpty(),
                        category = product.category.orEmpty(),
                        unitType = product.unitType.apiValue,
                        pricePerUnit = product.pricePerUnit.toString(),
                        availableQuantity = product.availableQuantity.toString(),
                        imageUrl = product.imageUrl.orEmpty()
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

    fun saveProduct() {
        val state = _uiState.value

        if (state.pickupLocations.isEmpty()) {
            _uiState.value = state.copy(
                errorMessage = "Create a pickup location before adding a product."
            )
            return
        }

        val pickupLocationId = state.pickupLocationId.toLongOrNull()
        if (pickupLocationId == null || pickupLocationId <= 0L) {
            _uiState.value = state.copy(errorMessage = "Select a pickup location.")
            return
        }

        if (state.name.trim().isBlank()) {
            _uiState.value = state.copy(errorMessage = "Enter product name.")
            return
        }

        val pricePerUnit = state.pricePerUnit.toDoubleOrNull()
        if (pricePerUnit == null || pricePerUnit < 0.0) {
            _uiState.value = state.copy(errorMessage = "Enter valid price per unit.")
            return
        }

        val availableQuantity = state.availableQuantity.toDoubleOrNull()
        if (availableQuantity == null || availableQuantity < 0.0) {
            _uiState.value = state.copy(errorMessage = "Enter valid available quantity.")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(
                isSaving = true,
                errorMessage = null,
                successMessage = null
            )

            val result = if (productId == null) {
                productRepository.createProduct(
                    pickupLocationId = pickupLocationId,
                    name = state.name.trim(),
                    description = state.description.trim().ifBlank { null },
                    category = state.category.trim().ifBlank { null },
                    unitType = state.unitType,
                    pricePerUnit = pricePerUnit,
                    availableQuantity = availableQuantity,
                    imageUrl = state.imageUrl.trim().ifBlank { null }
                )
            } else {
                productRepository.updateProduct(
                    id = productId,
                    pickupLocationId = pickupLocationId,
                    name = state.name.trim(),
                    description = state.description.trim().ifBlank { null },
                    category = state.category.trim().ifBlank { null },
                    unitType = state.unitType,
                    pricePerUnit = pricePerUnit,
                    availableQuantity = availableQuantity,
                    imageUrl = state.imageUrl.trim().ifBlank { null }
                )
            }

            when (result) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        successMessage = if (productId == null) {
                            "Product created successfully."
                        } else {
                            "Product updated successfully."
                        }
                    )
                    _navigation.emit(Unit)
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        errorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isSaving = true)
                }
            }
        }
    }

    fun deactivateProduct() {
        val id = productId ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDeleting = true,
                errorMessage = null,
                successMessage = null
            )

            when (val result = productRepository.deactivateProduct(id)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        successMessage = "Product deactivated successfully."
                    )
                    _navigation.emit(Unit)
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        errorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isDeleting = true)
                }
            }
        }
    }

    class Factory(
        private val productRepository: ProductRepository,
        private val pickupLocationRepository: PickupLocationRepository,
        private val productId: Long?
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FarmerProductFormViewModel(
                productRepository = productRepository,
                pickupLocationRepository = pickupLocationRepository,
                productId = productId
            ) as T
        }
    }
}