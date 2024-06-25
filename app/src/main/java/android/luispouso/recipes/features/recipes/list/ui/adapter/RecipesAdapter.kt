package android.luispouso.recipes.features.recipes.list.ui.adapter

import android.annotation.SuppressLint
import android.luispouso.recipes.core.ui.Throttler
import android.luispouso.recipes.core.ui.loadImageWithGlide
import android.luispouso.recipes.databinding.ItemRecipeBinding
import android.luispouso.recipes.features.recipes.list.domain.model.RecipeModel
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipesAdapter(private val listener: (RecipeModel) -> Unit) : RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {

    private var recipes: MutableList<RecipeModel> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<RecipeModel>) {
        val recipeListDiff = RecipeDiffUtil(recipes, newList)
        val result = DiffUtil.calculateDiff(recipeListDiff)
        recipes = newList as MutableList<RecipeModel>
        result.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.binding(recipes[position])
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    inner class RecipeViewHolder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {

        private val throttler = Throttler()
        fun binding(recipe: RecipeModel) {
            itemView.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    throttler.onClick {
                        listener(recipe)
                    }
                }
            }

            with(binding) {
                tvItemTitle.text = recipe.title
                tvItemDescription.text = recipe.description
                ivItem.loadImageWithGlide(Uri.parse(recipe.imageUrl))
            }
        }
    }
}
