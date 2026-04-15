package com.example.farmgate.data.remote.mapper

import com.example.farmgate.data.model.CancellationReason
import com.example.farmgate.data.model.Order
import com.example.farmgate.data.model.OrderItem
import com.example.farmgate.data.model.OrderStatus
import com.example.farmgate.data.model.Payment
import com.example.farmgate.data.model.PaymentStatus
import com.example.farmgate.data.model.UnitType
import com.example.farmgate.data.remote.dto.order.OrderDetailsDto
import com.example.farmgate.data.remote.dto.order.OrderItemDto
import com.example.farmgate.data.remote.dto.order.OrderSummaryDto
import com.example.farmgate.data.remote.dto.payment.PaymentDto

fun OrderSummaryDto.toModel(): Order {
    return Order(
        id = id,
        status = OrderStatus.fromInt(status),
        counterpartyName = counterpartyName,
        estimatedProductTotal = estimatedProductTotal,
        actualProductTotal = actualProductTotal,
        serviceFeeAmount = serviceFeeAmount,
        pickupDueAt = pickupDueAt,
        createdAt = createdAt
    )
}

fun OrderDetailsDto.toModel(): Order {
    return Order(
        id = id,
        status = OrderStatus.fromInt(status),
        cancellationReason = cancellationReason?.let { CancellationReason.fromInt(it) },
        customerName = customerName,
        farmerName = farmerName,
        estimatedProductTotal = estimatedProductTotal,
        actualProductTotal = actualProductTotal,
        serviceFeeAmount = serviceFeeAmount,
        feePaidAt = feePaidAt,
        pickupDueAt = pickupDueAt,
        pickupCity = pickupCity,
        pickupArea = pickupArea,
        pickupAddress = pickupAddress,
        pickupInstructions = pickupInstructions,
        farmerPhone = farmerPhone,
        createdAt = createdAt,
        items = items.map { it.toModel() },
        payment = payment?.toModel()
    )
}

fun OrderItemDto.toModel(): OrderItem {
    return OrderItem(
        id = id,
        productId = productId,
        productName = productName,
        unitType = UnitType.fromInt(unitType),
        unitPrice = unitPrice,
        orderedQuantity = orderedQuantity,
        fulfilledQuantity = fulfilledQuantity
    )
}

fun PaymentDto.toModel(): Payment {
    return Payment(
        id = id,
        orderId = orderId,
        amount = amount,
        status = PaymentStatus.fromInt(status),
        transactionReference = transactionReference,
        paidAt = paidAt
    )
}