package android.luispouso.recipes.features.recipes.list.ui

import android.luispouso.recipes.R
import android.luispouso.recipes.databinding.FragmentRecipesBinding
import android.luispouso.recipes.features.auth.ui.LoginActivity
import android.luispouso.recipes.features.recipes.list.ui.adapter.RecipesAdapter
import android.luispouso.recipes.features.recipes.list.vm.RecipesListViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesListFragment : Fragment() {

    private lateinit var binding: FragmentRecipesBinding
    private val recipeAdapter: RecipesAdapter by lazy {
        RecipesAdapter { recipe ->
            viewModel.onRecipeClick(recipe)
        }
    }
    private val viewModel: RecipesListViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setupRecyclerView()
        setListeners()
        viewModel.fetchRecipes()
    }

    private fun observeViewModel() {
        viewModel.recipesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                RecipesListViewModel.UiState.Loading -> binding.listLoading.visibility = View.VISIBLE
                is RecipesListViewModel.UiState.Success -> {
                    recipeAdapter.updateList(state.recipes)
                    binding.listLoading.visibility = View.GONE
                }

                is RecipesListViewModel.UiState.Error -> {
                    // TODO state.error?.message
                }
            }
        }

        viewModel.navigationEvent.observe(viewLifecycleOwner) { destination ->
            when (destination) {
                RecipesListViewModel.NavigationDestination.CreateRecipe -> findNavController().navigate(
                    RecipesListFragmentDirections.actionRecipesListFragmentDestinationToCreateRecipeFragment()
                )

                is RecipesListViewModel.NavigationDestination.RecipeDetail -> findNavController().navigate(
                    RecipesListFragmentDirections.actionRecipesListFragmentDestinationToDetailRecipeFragment(destination.recipeId)
                )

                RecipesListViewModel.NavigationDestination.ShowMenu -> showPopupMenu()
                RecipesListViewModel.NavigationDestination.SignOut -> LoginActivity.navigate(requireContext())
            }
        }
    }

    private fun setupRecyclerView() {

        binding.rvRecipe.adapter = recipeAdapter
    }

    private fun setListeners() {
        binding.fabRecipe.setOnClickListener {
            viewModel.onCreateRecipeClick()
        }

        binding.actionSettingsClose.setOnClickListener {
            viewModel.onMenuClick()
        }
    }

    private fun showPopupMenu() {
        val popup = PopupMenu(requireContext(), binding.actionSettingsClose).apply {
            menuInflater.inflate(R.menu.menu, this.menu)
            setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.action_close -> {
                        viewModel.onSignOutClick()
                        true
                    }

                    else -> false
                }
            }
        }
        popup.show()
    }
}