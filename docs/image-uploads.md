# Reusable image uploads

## Verified locations and integration

The farmer product form is used for both create and edit. It previously accepted `imageUrl`. Farmer and customer profile editors previously accepted `profileImageUrl`. These are the only editable image inputs found. Existing Coil displays in product cards, farmer management cards, product details (product and farmer avatar), order review, and profile headers remain read-only and continue to display existing URLs.

`media/ImageUploadField` provides gallery selection with `PickVisualMedia(ImageOnly)` and external camera capture using `TakePicture`. A nonexported FileProvider exposes only `cache/media/`. No camera/storage permission is requested. Camera output paths survive activity recreation via SavedStateHandle. Cancellation deletes abandoned captures; missing apps/access failures show actionable errors. Leaving an editor discards its local selection, while configuration recreation keeps its upload ViewModel. Cache files older than 24 hours are removed during image processing; compressed temporary files are removed on success/discard or ViewModel clearing.

`MediaUploadViewModel` owns processing/upload state and retry/discard transitions; Activity Result contracts stay in Compose. Processor and uploader are injectable interfaces. `AndroidImageProcessor` copies a bounded source on Dispatchers.IO, reads bounds, samples decoding to bound memory, applies EXIF rotation/mirroring, preserves aspect ratio, and never upscales. Products target max 1600px/~500KiB; avatars max 800px/~200KiB. Compression makes at most four dimension passes with five JPEG quality attempts per pass. Byte targets are best effort with a hard upload ceiling of 5MiB. Selection inputs are bounded to 25MiB. JPEG encoding removes original EXIF/GPS metadata.

`ImageRepository` uses Retrofit multipart field `file` and the existing AuthInterceptor. Upload API is `POST api/images/product|avatar`, response `{ "url": "https://…" }`. Only remote HTTP(S) URLs are written through the existing product/profile update APIs. Selection does not mutate the old URL before upload success. A busy/failed upload blocks form saving, and discard returns to the previous URL. A later form-save failure remains an error and retains the uploaded URL for retry. Profile/product image removal is explicit and only takes effect on form save. Image upload success is not reported as product/profile save success.

Logging now uses BASIC with the Authorization header redacted, avoiding multipart bodies and token logging. The existing base URL is reused; no cloud provider, credentials, or image-server hostname is hardcoded.

## Dependencies

Existing Activity Compose 1.12.2, Retrofit 2.11.0, OkHttp 4.12.0, Coil 2.7.0, and coroutines 1.8.1 are reused. Added AndroidX ExifInterface 1.4.1 (official artifact verified and resolved) for orientation, and coroutines-test 1.8.1 matching the existing runtime for tests. Kotlin 2.2.10, AGP 9.4.1, compileSdk 36, minSdk 24, and unrelated Compose versions are unchanged.

## Validation and outstanding device checks

Run from the repository with the prepared Android SDK/JDK environment:

```bash
bash gradlew compileDebugKotlin assembleDebug testDebugUnitTest lintDebug compileDebugAndroidTestKotlin --no-daemon
```

The local unit suite now has four tests total: the preexisting arithmetic test, aspect/dimension limits, upload failure-to-retry transitions and file lifetime, and restored-selection discard. An instrumentation test verifies actual sampled decode/compression, EXIF rotation, aspect ratio, and avatar targets; its Kotlin compiles here, but it has **not** been run on a device.

Manual/device acceptance still required: gallery and camera success/cancel, missing camera app, denied access, picker fallback on API 24, configuration change and process death during capture/upload, EXIF mirror variants, very large photos and memory pressure, offline retries, expired JWT, storage-full/backend failure, removing/cancelling an edit, and create/edit/profile persistence against the real deployed backend. This cloud machine has no connected emulator; adb's home-directory permissions and missing KVM support also prevent claiming device validation.

Backend uploads must be deployed and configured first. Configure its durable single-process image volume and `Images__PublicBaseUrl`; see the backend `FarmGate Backend/Media/README.md`. Set `FARMGATE_BASE_URL` in ignored `local.properties` to a device-reachable API origin ending in `/` and rebuild. No production hosting/storage provider has been inferred. Existing URLs remain compatible even when uploads are not configured; new upload attempts receive an explicit error rather than a fake URL.
