package com.example.farmgate.media

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Test
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class MediaTests {
    @Test fun dimensionsPreserveAspectAndNeverUpscale() {
        assertEquals(1600 to 800, ImageLimits.dimensions(4000, 2000, 1600))
        assertEquals(400 to 800, ImageLimits.dimensions(1000, 2000, 800))
        assertEquals(100 to 80, ImageLimits.dimensions(100, 80, 800))
        assertEquals(1 to 800, ImageLimits.dimensions(1, 100000, 800))
    }
    @Test fun uploadErrorBlocksSaveAndRetryKeepsFileUntilSuccess() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val file = File.createTempFile("media-test", ".jpg")
        try {
            var attempts = 0
            val model = MediaUploadViewModel(SavedStateHandle(mapOf("processed" to file.path)),
                ImageProcessor { _, _ -> error("Already processed") },
                ImageUploader { _, _ -> attempts++; if (attempts == 1) error("Offline") else "https://images.example/avatar.jpg" })
            assertTrue(model.state.value.blocked)
            model.retry("avatar")
            assertTrue(model.state.value.busy)
            advanceUntilIdle()
            assertEquals("Offline", model.state.value.error)
            assertTrue(file.exists())
            model.retry("avatar"); advanceUntilIdle()
            assertFalse(model.state.value.blocked)
            assertEquals("https://images.example/avatar.jpg", model.state.value.url)
            assertFalse(file.exists())
            model.consumeUrl(); assertNull(model.state.value.url)
        } finally { file.delete(); Dispatchers.resetMain() }
    }
    @Test fun discardRestoredSelectionUnblocksAndDeletesTemporaryFile() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val file = File.createTempFile("media-test", ".jpg")
        try {
            val model = MediaUploadViewModel(SavedStateHandle(mapOf("processed" to file.path)),
                ImageProcessor { _, _ -> error("Not called") }, ImageUploader { _, _ -> error("Not called") })
            model.discard()
            assertEquals(MediaState(), model.state.value)
            assertFalse(file.exists())
        } finally { file.delete(); Dispatchers.resetMain() }
    }
}
