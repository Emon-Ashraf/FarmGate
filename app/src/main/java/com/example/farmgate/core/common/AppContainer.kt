package com.example.farmgate.core.common

import android.content.Context
import com.example.farmgate.core.datastore.SessionManager
import com.example.farmgate.core.network.ApiClient
import com.example.farmgate.data.remote.api.OrdersApi
import com.example.farmgate.data.remote.api.ProductsApi
import com.example.farmgate.data.repository.AuthRepository
import com.example.farmgate.data.repository.CityRepository
import com.example.farmgate.data.repository.OrderDraftRepository
import com.example.farmgate.data.repository.OrderRepository
import com.example.farmgate.data.repository.ProductRepository
import com.example.farmgate.data.repository.ProfileRepository

class AppContainer(
    context: Context
) {
    private val appContext = context.applicationContext

    val sessionManager: SessionManager by lazy {
        SessionManager(appContext)
    }

    private val authApi by lazy {
        ApiClient.provideAuthApi(appContext)
    }

    private val profileApi by lazy {
        ApiClient.provideProfileApi(appContext)
    }

    private val citiesApi by lazy {
        ApiClient.provideCitiesApi(appContext)
    }

    val productsApi: ProductsApi by lazy {
        ApiClient.provideProductsApi(appContext)
    }

    val ordersApi: OrdersApi by lazy {
        ApiClient.provideOrdersApi(appContext)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository(
            authApi = authApi,
            sessionManager = sessionManager
        )
    }

    val profileRepository: ProfileRepository by lazy {
        ProfileRepository(profileApi = profileApi)
    }

    val cityRepository: CityRepository by lazy {
        CityRepository(citiesApi)
    }

    val productRepository: ProductRepository by lazy {
        ProductRepository(productsApi)
    }

    val orderRepository: OrderRepository by lazy {
        OrderRepository(ordersApi)
    }

    val orderDraftRepository: OrderDraftRepository by lazy {
        OrderDraftRepository()
    }
}