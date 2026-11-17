package com.example.farmgate.media

import android.graphics.Bitmap
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class ImageProcessorDeviceTest {
    @Test fun sampledCompressionCorrectsExifAndLimitsDimensions() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val input = File.createTempFile("orientation", ".jpg", context.cacheDir)
        try {
            val bitmap = Bitmap.createBitmap(2400, 1200, Bitmap.Config.ARGB_8888)
            input.outputStream().use { bitmap.compress(Bitmap.CompressFormat.JPEG, 95, it) }; bitmap.recycle()
            ExifInterface(input).apply { setAttribute(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_ROTATE_90.toString()); saveAttributes() }
            val output = AndroidImageProcessor(context).process(Uri.fromFile(input), ImageLimits.Avatar)
            try {
                val decoded = android.graphics.BitmapFactory.decodeFile(output.path)
                assertTrue(decoded.height > decoded.width)
                assertTrue(decoded.height <= 800)
                assertTrue(output.length() <= ImageLimits.Avatar.targetBytes)
                assertEquals(2.0, decoded.height.toDouble() / decoded.width, 0.02)
                decoded.recycle()
            } finally { output.delete() }
        } finally { input.delete() }
    }
}
