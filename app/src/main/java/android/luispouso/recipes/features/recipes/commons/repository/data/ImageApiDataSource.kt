package android.luispouso.recipes.features.recipes.commons.repository.data

import android.luispouso.recipes.core.app.AppResult
import android.net.Uri
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class ImageApiDataSource @Inject constructor(private val storageRef: StorageReference) {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun upLoadFile(localFilePath: Uri): AppResult<String> {
        val riversRef = storageRef.child("${localFilePath.lastPathSegment}")
        val uploadTask = riversRef.putFile(localFilePath)
        val uploadTaskResult = suspendCancellableCoroutine { continuation ->
            uploadTask.addOnSuccessListener {
                continuation.resume(AppResult.Success(Unit)) {}
            }.addOnFailureListener { exception ->
                continuation.resume(AppResult.Error(exception)) {}
            }
        }

        return if (uploadTaskResult is AppResult.Success) {
            downloadUrl(riversRef)
        } else {
            uploadTaskResult as AppResult.Error
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun downloadUrl(riversRef: StorageReference): AppResult<String> = suspendCancellableCoroutine { continuation ->
        riversRef.downloadUrl.addOnSuccessListener { uri ->
            continuation.resume(AppResult.Success(uri.toString())) {}
        }.addOnFailureListener { exception ->
            continuation.resume(AppResult.Error(exception)) {}
        }
    }
}
