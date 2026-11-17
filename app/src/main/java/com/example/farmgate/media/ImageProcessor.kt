package com.example.farmgate.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.roundToInt

data class ImageLimits(val maxEdge: Int, val targetBytes: Int) {
    companion object {
        val Product = ImageLimits(1600, 500 * 1024)
        val Avatar = ImageLimits(800, 200 * 1024)
        fun dimensions(width: Int, height: Int, max: Int): Pair<Int, Int> {
            require(width > 0 && height > 0 && max > 0)
            val ratio = minOf(1.0, max.toDouble() / maxOf(width, height))
            return maxOf(1, (width * ratio).roundToInt()) to maxOf(1, (height * ratio).roundToInt())
        }
    }
}

fun interface ImageProcessor {
    suspend fun process(source: Uri, limits: ImageLimits): File
}

class AndroidImageProcessor(private val context: Context) : ImageProcessor {
    override suspend fun process(source: Uri, limits: ImageLimits): File = withContext(Dispatchers.IO) {
        val directory = File(context.cacheDir, "media").apply { mkdirs() }
        // Expire abandoned files, including captures whose process was killed.
        directory.listFiles()?.filter { it.lastModified() < System.currentTimeMillis() - 86400000 }?.forEach { it.delete() }
        val input = File.createTempFile("source-", ".tmp", directory)
        val output = File.createTempFile("upload-", ".jpg", directory)
        var bitmap: Bitmap? = null
        try {
            context.contentResolver.openInputStream(source)?.use { stream ->
                input.outputStream().use { target ->
                    val buffer = ByteArray(8192); var total = 0
                    while (true) {
                        val size = stream.read(buffer); if (size < 0) break
                        total += size; require(total <= 25 * 1024 * 1024) { "Choose an image smaller than 25 MB." }
                        target.write(buffer, 0, size)
                    }
                }
            } ?: error("Cannot read this image. Choose it again.")
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeFile(input.path, bounds)
            require(bounds.outWidth > 0 && bounds.outHeight > 0) { "Cannot decode this image." }
            val options = BitmapFactory.Options().apply { inSampleSize = 1 }
            while (maxOf(bounds.outWidth, bounds.outHeight) / options.inSampleSize > limits.maxEdge * 2 ||
                bounds.outWidth.toLong() * bounds.outHeight / options.inSampleSize / options.inSampleSize > 8_000_000) {
                options.inSampleSize *= 2
            }
            bitmap = BitmapFactory.decodeFile(input.path, options) ?: error("Cannot decode this image.")
            val exif = ExifInterface(input)
            val matrix = Matrix().apply {
                if (exif.isFlipped) postScale(-1f, 1f)
                postRotate(exif.rotationDegrees.toFloat())
            }
            val oriented = Bitmap.createBitmap(bitmap!!, 0, 0, bitmap!!.width, bitmap!!.height, matrix, true)
            if (oriented !== bitmap) { bitmap!!.recycle(); bitmap = oriented }
            val (width, height) = ImageLimits.dimensions(bitmap!!.width, bitmap!!.height, limits.maxEdge)
            val scaled = Bitmap.createScaledBitmap(bitmap!!, width, height, true)
            if (scaled !== bitmap) { bitmap!!.recycle(); bitmap = scaled }
            // Bounded quality and dimension reductions, never increasing resolution.
            repeat(4) {
                for (quality in listOf(88, 78, 68, 58, 48)) {
                    output.outputStream().use { require(bitmap!!.compress(Bitmap.CompressFormat.JPEG, quality, it)) }
                    if (output.length() <= limits.targetBytes) return@withContext output
                }
                val smaller = Bitmap.createScaledBitmap(bitmap!!, maxOf(1, bitmap!!.width * 3 / 4), maxOf(1, bitmap!!.height * 3 / 4), true)
                if (smaller !== bitmap) { bitmap!!.recycle(); bitmap = smaller }
            }
            require(output.length() <= 5 * 1024 * 1024) { "Image is too large to upload." }
            output
        } catch (e: Throwable) { output.delete(); throw e }
        finally { bitmap?.recycle(); input.delete() }
    }
}
