package android.luispouso.recipes.features.recipes.detail.vm

import android.luispouso.recipes.features.recipes.commons.repository.RecipeRepository
import android.luispouso.recipes.features.recipes.list.domain.model.RecipeModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(private val recipeRepository: RecipeRepository) : ViewModel() {

    private val _recipe = MutableLiveData<RecipeModel>()
    val recipe: LiveData<RecipeModel> = _recipe

    fun fetchRecipeById(recipeId: String) {
        viewModelScope.launch {
            recipeRepository.getRecipeById(recipeId)?.let { recipe ->
                _recipe.value = recipe
            }
        }
    }
}