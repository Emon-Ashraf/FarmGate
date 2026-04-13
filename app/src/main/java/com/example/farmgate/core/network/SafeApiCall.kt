package com.example.farmgate.core.network


import com.example.farmgate.core.common.Resource
import retrofit2.Response
import java.io.IOException

suspend fun <T, R> safeApiCall(
    apiCall: suspend () -> Response<T>,
    mapper: (T) -> R
): Resource<R> {
    return try {
        val response = apiCall()

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Resource.Success(mapper(body))
            } else {
                Resource.Error(message = "Response body is empty.", code = response.code())
            }
        } else {
            Resource.Error(
                message = response.errorBody()?.string().orEmpty().ifBlank { "Request failed." },
                code = response.code()
            )
        }
    } catch (exception: IOException) {
        Resource.Error(message = "Network error. Please check your connection.")
    } catch (exception: Exception) {
        Resource.Error(message = exception.message ?: "Unexpected error occurred.")
    }
}