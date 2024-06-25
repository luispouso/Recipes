package android.luispouso.recipes.features.recipes.commons.repository.data

import android.luispouso.recipes.core.app.AppResult
import android.luispouso.recipes.features.recipes.list.domain.model.RecipeModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class RecipeApiDataSource @Inject constructor(private val db: FirebaseFirestore) {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun saveRecipe(recipe: RecipeModel): AppResult<Unit> = suspendCancellableCoroutine { continuation ->
        val result = db.collection("recipes").document(recipe.id).set(recipe.getHash())
        result.addOnSuccessListener {
            continuation.resume(AppResult.Success(Unit)) {}
        }.addOnFailureListener { exception ->
            continuation.resume(AppResult.Error(exception)) {}
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getRecipes(): AppResult<List<RecipeModel>> = suspendCancellableCoroutine { continuation ->
        val result = db.collection("recipes").orderBy("title", Query.Direction.ASCENDING).get()
        result.addOnSuccessListener { documents ->
            val recipes: List<RecipeModel> = documents.map {
                it.toObject(RecipeModel::class.java)
            }
            continuation.resume(AppResult.Success(recipes)) {}
        }
            .addOnFailureListener { exception ->
                continuation.resume(AppResult.Error(exception)) {}
            }
    }
}