package com.example.farmgate.core.network

import android.content.Context
import com.example.farmgate.BuildConfig
import com.example.farmgate.core.datastore.SessionManager
import com.example.farmgate.data.remote.api.AuthApi
import com.example.farmgate.data.remote.api.CitiesApi
import com.example.farmgate.data.remote.api.OrdersApi
import com.example.farmgate.data.remote.api.ProductsApi
import com.example.farmgate.data.remote.api.ProfileApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private fun provideOkHttpClient(
        sessionManager: SessionManager
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .addInterceptor(provideLoggingInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private fun provideRetrofit(
        sessionManager: SessionManager
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(provideOkHttpClient(sessionManager))
            .addConverterFactory(GsonConverterFactory.create(provideGson()))
            .build()
    }

    fun provideAuthApi(context: Context): AuthApi {
        val sessionManager = SessionManager(context.applicationContext)
        return provideRetrofit(sessionManager).create(AuthApi::class.java)
    }

    fun provideProfileApi(context: Context): ProfileApi {
        val sessionManager = SessionManager(context.applicationContext)
        return provideRetrofit(sessionManager).create(ProfileApi::class.java)
    }

    fun provideCitiesApi(context: Context): CitiesApi {
        val sessionManager = SessionManager(context.applicationContext)
        return provideRetrofit(sessionManager).create(CitiesApi::class.java)
    }

    fun provideProductsApi(context: Context): ProductsApi {
        val sessionManager = SessionManager(context.applicationContext)
        return provideRetrofit(sessionManager).create(ProductsApi::class.java)
    }

    fun provideOrdersApi(context: Context): OrdersApi {
        val sessionManager = SessionManager(context.applicationContext)
        return provideRetrofit(sessionManager).create(OrdersApi::class.java)
    }
}