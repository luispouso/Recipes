package android.luispouso.recipes.core.app

sealed class AppResult<out T> {
    data class Success<out T>(val value: T) : AppResult<T>()
    data class Error(val ex: Exception) : AppResult<Nothing>()

    suspend fun <R> fold(
        onSuccess: suspend (data: T) -> R,
        onError: suspend (error: Exception) -> R,
    ): R {
        return if (this is Success) {
            onSuccess(this.value)
        } else {
            onError((this as Error).ex)
        }
    }
}