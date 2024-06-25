package android.luispouso.recipes.features.recipes.creator.vm

import android.luispouso.recipes.R
import android.luispouso.recipes.core.app.AppResult
import android.luispouso.recipes.core.ui.ResourceProvider
import android.luispouso.recipes.core.ui.Throttler
import android.luispouso.recipes.features.recipes.commons.repository.RecipeRepository
import android.luispouso.recipes.features.recipes.list.domain.model.RecipeModel
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val resourceProvider: ResourceProvider,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    sealed class UiState {
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    private val _selectedImageUri = MutableLiveData<Uri?>()
    val selectedImageUri: LiveData<Uri?> = _selectedImageUri

    private val throttler = Throttler()

    fun setSelectedImageUri(uri: Uri?) {
        _selectedImageUri.value = uri
    }

        fun saveRecipeWithValidation(
            title: String,
            author: String,
            duration: String,
            description: String,
            ingredient: String,
            preparation: String,
        ) {
            viewModelScope.launch {
                if (!isNetworkAvailable()) {
                    _uiState.value = UiState.Error(resourceProvider.getString(R.string.no_internet))
                    return@launch
                }

                val imageUri = selectedImageUri.value

                if (areAllFieldsFilled(title, author, duration, description, ingredient, preparation, imageUri)) {
                    throttler.onClick {
                        _uiState.value = UiState.Loading
                        val recipe = createRecipe(title, author, duration, description, ingredient, preparation)
                        when (val result = recipeRepository.saveRecipe(recipe, imageUri!!)) {
                            is AppResult.Success -> {
                                _uiState.value = UiState.Success
                            }
                            is AppResult.Error -> {
                                _uiState.value = UiState.Error(result.ex.message ?: resourceProvider.getString(R.string.unknown_error))
                            }
                        }
                    }
                } else {
                    _uiState.value = UiState.Error(resourceProvider.getString(R.string.empty_fields))
                }
            }
        }

    private fun areAllFieldsFilled(
        title: String,
        author: String,
        duration: String,
        description: String,
        ingredient: String,
        preparation: String,
        selectedImageUri: Uri?
    ): Boolean {
        return title.isNotBlank() && selectedImageUri != null && author.isNotBlank() &&
            duration.isNotBlank() && description.isNotBlank() && ingredient.isNotBlank() &&
            preparation.isNotBlank()
    }

    private fun createRecipe(
        title: String,
        author: String,
        duration: String,
        description: String,
        ingredient: String,
        preparation: String
    ): RecipeModel {
        return RecipeModel(
            title = title,
            imageUrl = "",
            author = author,
            duration = duration,
            description = description,
            ingredient = ingredient,
            preparation = preparation
        )
    }

    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}