package com.example.farmgate.core.common


import android.content.Context
import com.example.farmgate.core.datastore.SessionManager
import com.example.farmgate.core.network.ApiClient
import com.example.farmgate.data.repository.AuthRepository
import com.example.farmgate.data.repository.CitiesRepository
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

    val authRepository: AuthRepository by lazy {
        AuthRepository(
            authApi = authApi,
            sessionManager = sessionManager
        )
    }

    val profileRepository: ProfileRepository by lazy {
        ProfileRepository(profileApi = profileApi)
    }

    val citiesRepository: CitiesRepository by lazy {
        CitiesRepository(citiesApi = citiesApi)
    }
}