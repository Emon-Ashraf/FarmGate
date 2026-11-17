package com.example.farmgate.media

import android.content.ActivityNotFoundException
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.compose.AsyncImage
import com.example.farmgate.core.network.ApiClient
import java.io.File

@Composable
fun ImageUploadField(
    currentUrl: String,
    purpose: String,
    enabled: Boolean,
    onUrlChanged: (String) -> Unit,
    onBlockedChanged: (Boolean) -> Unit
) {
    val uiContext = LocalContext.current
    val context = uiContext.applicationContext
    val model: MediaUploadViewModel = viewModel(key = "image-upload-$purpose", factory = viewModelFactory {
        initializer { MediaUploadViewModel(createSavedStateHandle(), AndroidImageProcessor(context), ImageRepository(ApiClient.provideImagesApi(context))) }
    })
    val state by model.state.collectAsState()
    val urlCallback by rememberUpdatedState(onUrlChanged)
    val blockedCallback by rememberUpdatedState(onBlockedChanged)
    DisposableEffect(model) {
        onDispose {
            var owner: android.content.Context? = uiContext
            while (owner is android.content.ContextWrapper && owner !is android.app.Activity) owner = owner.baseContext
            if ((owner as? android.app.Activity)?.isChangingConfigurations != true) {
                model.discard()
                blockedCallback(false)
            }
        }
    }
    LaunchedEffect(state) {
        blockedCallback(state.blocked)
        state.url?.let { urlCallback(it); model.consumeUrl() }
    }
    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        val path = model.cameraPath
        if (success && path != null) model.select(FileProvider.getUriForFile(context, "${context.packageName}.media", File(path)), purpose)
        else { path?.let { File(it).delete() }; model.cameraPath = null }
    }
    val gallery = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) model.select(uri, purpose)
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        val preview = state.preview ?: currentUrl.takeIf { it.isNotBlank() }
        if (preview != null) AsyncImage(model = preview, contentDescription = "Selected image", modifier = Modifier.fillMaxWidth().height(160.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(enabled = enabled && !state.busy, onClick = {
                try { gallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
                catch (e: SecurityException) { model.selectionError("Cannot access photos. Try selecting again.") }
                catch (e: ActivityNotFoundException) { model.selectionError("No photo picker is available.") }
            }) { Text("Gallery") }
            OutlinedButton(enabled = enabled && !state.busy, onClick = {
                try {
                    val dir = File(context.cacheDir, "media").apply { mkdirs() }
                    model.cameraPath?.let { File(it).delete() }
                    val file = File.createTempFile("camera-", ".jpg", dir)
                    model.cameraPath = file.path
                    camera.launch(FileProvider.getUriForFile(context, "${context.packageName}.media", file))
                } catch (e: Exception) {
                    model.cameraPath?.let { File(it).delete() }; model.cameraPath = null
                    model.selectionError(if (e is SecurityException) "Camera access was denied." else "No camera application is available.")
                }
            }) { Text("Camera") }
        }
        if (state.busy) { LinearProgressIndicator(modifier = Modifier.fillMaxWidth()); Text("Preparing and uploading image…") }
        state.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Row {
                TextButton(onClick = { model.retry(purpose) }, enabled = enabled && state.canRetry) { Text("Retry upload") }
                TextButton(onClick = model::discard, enabled = enabled) { Text("Discard selection") }
            }
        }
        if (currentUrl.isNotBlank() && !state.busy && state.error == null) {
            TextButton(enabled = enabled, onClick = { model.discard(); onUrlChanged("") }) { Text("Remove image") }
        }
        Text("Your image is saved with the form after upload finishes.", style = MaterialTheme.typography.bodySmall)
    }
}
