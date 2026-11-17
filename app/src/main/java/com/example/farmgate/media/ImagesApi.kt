package com.example.farmgate.media

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

data class UploadedImage(val url: String)
interface ImagesApi {
    @Multipart @POST("api/images/{purpose}")
    suspend fun upload(@Path("purpose") purpose: String, @Part file: MultipartBody.Part): Response<UploadedImage>
}
fun interface ImageUploader { suspend fun upload(file: File, purpose: String): String }
class ImageRepository(private val api: ImagesApi) : ImageUploader {
    override suspend fun upload(file: File, purpose: String): String {
        val response = api.upload(purpose, MultipartBody.Part.createFormData("file", "image.jpg", file.asRequestBody("image/jpeg".toMediaType())))
        if (!response.isSuccessful) error(when(response.code()) {
            401 -> "Your session expired. Sign in again."
            413 -> "Image is too large. Choose another image."
            503 -> "Image storage is unavailable. Try again later."
            429 -> "Too many uploads. Wait a minute and retry."
            else -> "Upload failed (${response.code()}). Retry or choose another image."
        })
        val url = response.body()?.url ?: error("Upload returned no image URL.")
        require(url.startsWith("https://") || url.startsWith("http://")) { "Upload returned an invalid image URL." }
        return url
    }
}
