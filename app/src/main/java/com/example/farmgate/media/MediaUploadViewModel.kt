package com.example.farmgate.media

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

data class MediaState(val preview: String? = null, val url: String? = null, val busy: Boolean = false, val error: String? = null, val canRetry: Boolean = false) {
    val blocked: Boolean get() = busy || error != null
}

class MediaUploadViewModel(
    private val saved: SavedStateHandle,
    private val processor: ImageProcessor,
    private val uploader: ImageUploader
) : ViewModel() {
    private var job: Job? = null
    private var processed: File? = saved.get<String>("processed")?.let(::File)?.takeIf { it.exists() }
    private val _state = MutableStateFlow(MediaState(
        preview = processed?.toURI()?.toString(),
        canRetry = processed != null,
        error = if (processed != null) "Image is not uploaded. Retry or discard it." else null
    ))
    val state = _state.asStateFlow()
    var cameraPath: String?
        get() = saved["camera"]
        set(value) { saved["camera"] = value }

    fun select(uri: Uri, purpose: String) {
        if (_state.value.busy) return
        clearFiles()
        val capture = cameraPath
        cameraPath = null
        _state.value = MediaState(preview = uri.toString(), busy = true)
        job = viewModelScope.launch {
            try {
                processed = processor.process(uri, if (purpose == "product") ImageLimits.Product else ImageLimits.Avatar)
                saved["processed"] = processed!!.path
                _state.value = MediaState(preview = processed!!.toURI().toString(), busy = true, canRetry = true)
                upload(purpose)
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { _state.value = _state.value.copy(busy = false, error = e.message ?: "Cannot prepare image.") }
            finally { capture?.let { File(it).delete() } }
        }
    }
    fun retry(purpose: String) {
        if (_state.value.busy || processed == null) return
        _state.value = _state.value.copy(busy = true, error = null)
        job = viewModelScope.launch { upload(purpose) }
    }
    private suspend fun upload(purpose: String) {
        try {
            val url = uploader.upload(requireNotNull(processed), purpose)
            _state.value = MediaState(url = url)
            clearFiles()
        } catch (e: CancellationException) { throw e }
        catch (e: Exception) { _state.value = _state.value.copy(busy = false, error = e.message ?: "Upload failed. Retry.") }
    }
    fun consumeUrl() { _state.value = _state.value.copy(url = null) }
    fun selectionError(message: String) { _state.value = MediaState(error = message) }
    fun discard() { job?.cancel(); clearFiles(); _state.value = MediaState() }
    private fun clearFiles() {
        processed?.delete(); processed = null; saved["processed"] = null
    }
    override fun onCleared() { clearFiles(); cameraPath?.let { File(it).delete() } }
}
