package android.luispouso.recipes.features.recipes.creator.ui

import android.app.Activity
import android.content.Intent
import android.luispouso.recipes.R
import android.luispouso.recipes.core.ui.loadImageWithGlide
import android.luispouso.recipes.core.ui.showToast
import android.luispouso.recipes.databinding.FragmentCreateRecipeBinding
import android.luispouso.recipes.features.recipes.creator.vm.CreateRecipeViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateRecipeFragment : Fragment() {

    private lateinit var binding: FragmentCreateRecipeBinding
    private lateinit var startForProfileImageResult: ActivityResultLauncher<Intent>
    private val viewModel: CreateRecipeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerActivityResultLauncher()
        setupObservers()
        saveCreatedRecipe()
        goBack()
    }

    private fun saveCreatedRecipe() {
        with(binding) {
            btnSaveRecipe.setOnClickListener {
                val title = etTitle.text.toString()
                val author = etAuthor.text.toString()
                val duration = etDuration.text.toString()
                val description = etDescription.text.toString()
                val ingredient = etIngredient.text.toString()
                val preparation = etPreparation.text.toString()

                viewModel.saveRecipeWithValidation(
                    title, author, duration, description, ingredient, preparation
                )
            }
            ivNewRecipe.setOnClickListener {
                launchImagePicker()
            }
        }
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is CreateRecipeViewModel.UiState.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }

                is CreateRecipeViewModel.UiState.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    findNavController().popBackStack(R.id.recipesListFragmentDestination, false)
                }

                is CreateRecipeViewModel.UiState.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    showToast(uiState.message)
                }
            }
        }

        viewModel.selectedImageUri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                binding.ivNewRecipe.loadImageWithGlide(it)
            }
        }
    }

    private fun launchImagePicker() {
        ImagePicker.with(this)
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    private fun registerActivityResultLauncher() {
        startForProfileImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri = result.data?.data
                viewModel.setSelectedImageUri(selectedImageUri)
            }
        }
    }

    private fun goBack() {
        binding.ivBackCreate.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
