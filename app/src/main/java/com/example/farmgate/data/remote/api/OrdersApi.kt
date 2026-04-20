package com.example.farmgate.data.remote.api

import com.example.farmgate.data.remote.dto.order.CancelOrderDto
import com.example.farmgate.data.remote.dto.order.CompleteOrderDto
import com.example.farmgate.data.remote.dto.order.ConfirmServiceFeeDto
import com.example.farmgate.data.remote.dto.order.CreateOrderDto
import com.example.farmgate.data.remote.dto.order.OrderDetailsDto
import com.example.farmgate.data.remote.dto.order.OrderSummaryDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface OrdersApi {

    @POST("api/orders")
    suspend fun createOrder(
        @Body request: CreateOrderDto
    ): Response<OrderDetailsDto>

    @GET("api/orders/customer")
    suspend fun getCustomerOrders(): Response<List<OrderSummaryDto>>

    @GET("api/orders/farmer")
    suspend fun getFarmerOrders(): Response<List<OrderSummaryDto>>

    @GET("api/orders/{id}")
    suspend fun getOrderDetails(
        @Path("id") orderId: Long
    ): Response<OrderDetailsDto>

    @PATCH("api/orders/{id}/accept")
    suspend fun acceptOrder(
        @Path("id") orderId: Long
    ): Response<OrderDetailsDto>

    @PATCH("api/orders/{id}/reject")
    suspend fun rejectOrder(
        @Path("id") orderId: Long
    ): Response<OrderDetailsDto>

    @PATCH("api/orders/{id}/cancel")
    suspend fun cancelOrder(
        @Path("id") orderId: Long,
        @Body request: CancelOrderDto? = null
    ): Response<OrderDetailsDto>

    @PATCH("api/orders/{id}/fee/confirm")
    suspend fun confirmServiceFee(
        @Path("id") orderId: Long,
        @Body request: ConfirmServiceFeeDto
    ): Response<OrderDetailsDto>

    @PATCH("api/orders/{id}/complete")
    suspend fun completeOrder(
        @Path("id") orderId: Long,
        @Body request: CompleteOrderDto
    ): Response<OrderDetailsDto>
}