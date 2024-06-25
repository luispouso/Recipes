package android.luispouso.recipes.features.recipes.commons.repository

import android.luispouso.recipes.core.app.AppResult
import android.luispouso.recipes.features.recipes.commons.repository.data.ImageApiDataSource
import android.luispouso.recipes.features.recipes.commons.repository.data.RecipeApiDataSource
import android.luispouso.recipes.features.recipes.list.domain.model.RecipeModel
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(
    private val recipeApiDataSource: RecipeApiDataSource,
    private val imageApiDataSource: ImageApiDataSource,
) {
    private val recipesCache: MutableList<RecipeModel> = mutableListOf()
    suspend fun saveRecipe(recipe: RecipeModel, imageUri: Uri): AppResult<Unit> {
        val recipeImageUrlUpdatedResult = imageApiDataSource.upLoadFile(imageUri)
        return if (recipeImageUrlUpdatedResult is AppResult.Success) {
            val recipeWithImageUpdated = recipe.copy(imageUrl = recipeImageUrlUpdatedResult.value)
            recipeApiDataSource.saveRecipe(recipeWithImageUpdated)
        } else {
            recipeImageUrlUpdatedResult as AppResult.Error
        }
    }

    suspend fun getRecipes(): AppResult<List<RecipeModel>> {
        return withContext(Dispatchers.IO) {
            val recipesResult = recipeApiDataSource.getRecipes()
            if (recipesResult is AppResult.Success) {
                recipesCache.clear()
                recipesCache.addAll(recipesResult.value)
            }
            recipesResult
        }
    }

    fun getRecipeById(recipeId: String): RecipeModel? {
        return recipesCache.find { it.id == recipeId }
    }
}