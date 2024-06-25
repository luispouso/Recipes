package android.luispouso.recipes.features.recipes.list.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class RecipeModel(
    val id: String = UUID.randomUUID().toString(),
    val author: String = "",
    val duration: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val ingredient: String = "",
    val preparation: String = "",
) : Parcelable {

    fun getHash(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "title" to title,
            "description" to description,
            "duration" to duration,
            "author" to author,
            "imageUrl" to imageUrl,
            "ingredient" to ingredient,
            "preparation" to preparation
        )
    }
}
