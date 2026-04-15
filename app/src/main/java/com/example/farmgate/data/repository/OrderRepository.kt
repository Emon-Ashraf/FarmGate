package com.example.farmgate.data.repository

import com.example.farmgate.core.common.Resource
import com.example.farmgate.core.network.safeApiCall
import com.example.farmgate.data.model.Order
import com.example.farmgate.data.model.OrderDraft
import com.example.farmgate.data.remote.api.OrdersApi
import com.example.farmgate.data.remote.dto.order.CancelOrderDto
import com.example.farmgate.data.remote.dto.order.ConfirmServiceFeeDto
import com.example.farmgate.data.remote.dto.order.CreateOrderDto
import com.example.farmgate.data.remote.dto.order.OrderItemRequestDto
import com.example.farmgate.data.remote.mapper.toModel

class OrderRepository(
    private val ordersApi: OrdersApi
) {

    suspend fun createOrder(
        draft: OrderDraft
    ): Resource<Order> {
        return safeApiCall(
            apiCall = {
                ordersApi.createOrder(
                    CreateOrderDto(
                        pickupLocationId = draft.pickupLocationId,
                        items = draft.items.map {
                            OrderItemRequestDto(
                                productId = it.productId,
                                quantity = it.selectedQuantity
                            )
                        }
                    )
                )
            },
            mapper = { dto -> dto.toModel() }
        )
    }

    suspend fun getCustomerOrders(): Resource<List<Order>> {
        return safeApiCall(
            apiCall = { ordersApi.getCustomerOrders() },
            mapper = { dtoList -> dtoList.map { it.toModel() } }
        )
    }

    suspend fun getOrderDetails(orderId: Long): Resource<Order> {
        return safeApiCall(
            apiCall = { ordersApi.getOrderDetails(orderId) },
            mapper = { dto -> dto.toModel() }
        )
    }

    suspend fun cancelOrder(
        orderId: Long,
        note: String? = null
    ): Resource<Order> {
        return safeApiCall(
            apiCall = {
                ordersApi.cancelOrder(
                    orderId = orderId,
                    request = CancelOrderDto(note = note)
                )
            },
            mapper = { dto -> dto.toModel() }
        )
    }

    suspend fun confirmServiceFee(
        orderId: Long,
        paymentReference: String?
    ): Resource<Order> {
        return safeApiCall(
            apiCall = {
                ordersApi.confirmServiceFee(
                    orderId = orderId,
                    request = ConfirmServiceFeeDto(
                        paymentReference = paymentReference
                    )
                )
            },
            mapper = { dto -> dto.toModel() }
        )
    }
}