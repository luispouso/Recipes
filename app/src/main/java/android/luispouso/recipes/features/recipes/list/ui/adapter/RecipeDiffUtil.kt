package android.luispouso.recipes.features.recipes.list.ui.adapter

import android.luispouso.recipes.features.recipes.list.domain.model.RecipeModel
import androidx.recyclerview.widget.DiffUtil

class RecipeDiffUtil(
    private val oldList: List<RecipeModel>,
    private val newList: List<RecipeModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}