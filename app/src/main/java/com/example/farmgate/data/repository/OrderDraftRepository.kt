package com.example.farmgate.data.repository


import com.example.farmgate.data.model.OrderDraft
import com.example.farmgate.data.model.OrderDraftItem
import com.example.farmgate.data.model.ProductDetails
import com.example.farmgate.data.model.UnitType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrderDraftRepository {

    private val _draft = MutableStateFlow<OrderDraft?>(null)
    val draft: StateFlow<OrderDraft?> = _draft.asStateFlow()

    fun addProduct(
        product: ProductDetails,
        quantity: Double
    ): DraftAddResult {
        if (quantity <= 0.0) return DraftAddResult.InvalidQuantity

        val requiresInteger = product.unitType == UnitType.Piece ||
                product.unitType == UnitType.Dozen ||
                product.unitType == UnitType.Bundle

        if (requiresInteger && quantity != quantity.toInt().toDouble()) {
            return DraftAddResult.InvalidQuantity
        }

        val currentDraft = _draft.value

        if (currentDraft == null) {
            _draft.value = OrderDraft(
                pickupLocationId = product.pickupLocationId,
                farmerName = product.farmerName,
                pickupArea = product.pickupArea,
                cityName = product.cityName,
                pickupAddress = product.pickupAddress,
                items = listOf(
                    OrderDraftItem(
                        productId = product.id,
                        productName = product.name,
                        unitType = product.unitType,
                        pricePerUnit = product.pricePerUnit,
                        availableQuantity = product.availableQuantity,
                        selectedQuantity = quantity,
                        pickupLocationId = product.pickupLocationId,
                        imageUrl = product.imageUrl

                    )
                )
            )
            return DraftAddResult.Added
        }

        if (currentDraft.pickupLocationId != product.pickupLocationId) {
            return DraftAddResult.Conflict
        }

        val existingItem = currentDraft.items.firstOrNull { it.productId == product.id }

        val updatedItems = if (existingItem == null) {
            currentDraft.items + OrderDraftItem(
                productId = product.id,
                productName = product.name,
                unitType = product.unitType,
                pricePerUnit = product.pricePerUnit,
                availableQuantity = product.availableQuantity,
                selectedQuantity = quantity,
                pickupLocationId = product.pickupLocationId,
                imageUrl = product.imageUrl

            )
        } else {
            currentDraft.items.map {
                if (it.productId == product.id) {
                    it.copy(selectedQuantity = it.selectedQuantity + quantity)
                } else {
                    it
                }
            }
        }

        _draft.value = currentDraft.copy(items = updatedItems)
        return DraftAddResult.Added
    }

    fun replaceWithSingleProduct(
        product: ProductDetails,
        quantity: Double
    ) {
        _draft.value = OrderDraft(
            pickupLocationId = product.pickupLocationId,
            farmerName = product.farmerName,
            pickupArea = product.pickupArea,
            cityName = product.cityName,
            pickupAddress = product.pickupAddress,
            items = listOf(
                OrderDraftItem(
                    productId = product.id,
                    productName = product.name,
                    unitType = product.unitType,
                    pricePerUnit = product.pricePerUnit,
                    availableQuantity = product.availableQuantity,
                    selectedQuantity = quantity,
                    pickupLocationId = product.pickupLocationId,
                    imageUrl = product.imageUrl

                )
            )
        )
    }

    fun updateItemQuantity(productId: Long, quantity: Double) {
        val currentDraft = _draft.value ?: return

        val updatedItems = currentDraft.items.mapNotNull { item ->
            if (item.productId != productId) return@mapNotNull item

            if (quantity <= 0.0) return@mapNotNull null

            val requiresInteger = item.unitType == UnitType.Piece ||
                    item.unitType == UnitType.Dozen ||
                    item.unitType == UnitType.Bundle

            if (requiresInteger && quantity != quantity.toInt().toDouble()) {
                return@mapNotNull item
            }

            item.copy(selectedQuantity = quantity)
        }

        _draft.value = if (updatedItems.isEmpty()) {
            null
        } else {
            currentDraft.copy(items = updatedItems)
        }
    }

    fun removeItem(productId: Long) {
        val currentDraft = _draft.value ?: return
        val updatedItems = currentDraft.items.filterNot { it.productId == productId }

        _draft.value = if (updatedItems.isEmpty()) {
            null
        } else {
            currentDraft.copy(items = updatedItems)
        }
    }

    fun clearDraft() {
        _draft.value = null
    }
}

enum class DraftAddResult {
    Added,
    Conflict,
    InvalidQuantity
}