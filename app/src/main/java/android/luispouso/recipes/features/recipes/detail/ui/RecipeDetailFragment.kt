package android.luispouso.recipes.features.recipes.detail.ui

import android.luispouso.recipes.R
import android.luispouso.recipes.core.ui.loadImageWithGlide
import android.luispouso.recipes.databinding.FragmentRecipeDetailBinding
import android.luispouso.recipes.features.recipes.detail.vm.RecipeDetailViewModel
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailFragment : Fragment() {

    private lateinit var binding: FragmentRecipeDetailBinding
    private val args: RecipeDetailFragmentArgs by navArgs()
    private val viewModel: RecipeDetailViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRecipeDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchRecipeById(args.recipeId)
        setupObservers()
        goBack()
    }

    private fun setupObservers() {
        viewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            with(binding) {
                tvToolbar.text = recipe.title
                tvDescription.text = recipe.description
                ivRecipe.loadImageWithGlide(Uri.parse(recipe.imageUrl))
                tvIngredient.text = recipe.ingredient
                tvPreparation.text = recipe.preparation
                tvDuration.text = getString(R.string.duration_format, recipe.duration)
                tvAuthor.text = getString(R.string.created_by_format, recipe.author)
            }
        }
    }

    private fun goBack() {
        binding.arrowBackDetail.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}