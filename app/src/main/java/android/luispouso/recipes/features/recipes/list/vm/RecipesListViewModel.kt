package android.luispouso.recipes.features.recipes.list.vm

import android.luispouso.recipes.core.app.AppResult
import android.luispouso.recipes.core.ui.SingleLiveData
import android.luispouso.recipes.features.recipes.commons.repository.RecipeRepository
import android.luispouso.recipes.features.recipes.list.domain.model.RecipeModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel @Inject constructor(private val recipeRepository: RecipeRepository) : ViewModel() {

    sealed class UiState {
        object Loading : UiState()
        data class Success(val recipes: List<RecipeModel>) : UiState()
        data class Error(val error: Throwable?) : UiState()
    }

    sealed class NavigationDestination {
        object SignOut : NavigationDestination()
        data class RecipeDetail(val recipeId: String) : NavigationDestination()
        object CreateRecipe : NavigationDestination()
        object ShowMenu : NavigationDestination()
    }

    private val _recipesState = MutableLiveData<UiState>()
    val recipesState: LiveData<UiState> = _recipesState
    val navigationEvent = SingleLiveData<NavigationDestination>()

    fun fetchRecipes() {
        _recipesState.value = UiState.Loading
        viewModelScope.launch {
            when (val result = recipeRepository.getRecipes()) {
                is AppResult.Success -> _recipesState.value = UiState.Success(result.value)
                is AppResult.Error -> _recipesState.value = UiState.Error(result.ex)
            }
        }
    }

    fun onSignOutClick() {
        navigationEvent.value = NavigationDestination.SignOut
        FirebaseAuth.getInstance().signOut()
    }

    fun onCreateRecipeClick() {
        navigationEvent.value = NavigationDestination.CreateRecipe
    }

    fun onRecipeClick(recipe: RecipeModel) {
        navigationEvent.value = NavigationDestination.RecipeDetail(recipeId = recipe.id)
    }

    fun onMenuClick() {
        navigationEvent.value = NavigationDestination.ShowMenu
    }
}