package de.fherfurt.onlyoneegg.view.ui.recipe

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.databinding.FragmentRecipeBinding
import de.fherfurt.onlyoneegg.storage.IngredientRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase


/*
* Recipe Fragment shows the recipe view with its ingredients
* */
class RecipeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //initialize a recipeId from nav args
        val args: RecipeFragmentArgs by navArgs()
        val recipeId = args.recipeId

        // Show the Fragment in Landscape or Portrait Modes
        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentRecipeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_recipe, container, false
        )

        // Get a Reference to the Appliction
        val application = requireNotNull(this.activity).application

        // Get a reference to recipe/ingredient/cookbook Dao
        val recipeDao = OOEDatabase.getInstance(application).recipeDao;

        val ingredientDao = OOEDatabase.getInstance(application).ingredientDao;

        val cookbookDao = OOEDatabase.getInstance(application).cookbookDao

        // Get a reference to ingredient Repository
        val ingredientRepository = IngredientRepository(ingredientDao)

        // Setup  ViewModel
        val recipeViewModel = RecipeViewModel(application, ingredientRepository, recipeId)

        // Catch changes on LiveData
        binding.lifecycleOwner = this

        // Bind the Recipe Fragment with the RecipeViewModel
        binding.recipeViewModel = recipeViewModel

        // Fetch the ingredient list from the adapter to the Recipe Fragment
        val adapter = RecipeAdapter()
        binding.ingredientList.adapter = adapter

        recipeViewModel.ingredients.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        // Set the Layout to the recyclerview
        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        binding.ingredientList.layoutManager = manager

        // Show the Recipe Name
        recipeViewModel.recipe.observe(
            viewLifecycleOwner,
            Observer { newRecipe -> binding.recipeName.text = newRecipe.name })

        // Show the Recipe Difficulty
        recipeViewModel.recipe.observe(
            viewLifecycleOwner,
            Observer { newRecipe -> binding.difficulty.text = newRecipe.difficulty.toString() })

        // Show the Recipe Cooktime
        recipeViewModel.recipe.observe(
            viewLifecycleOwner,
            Observer { newRecipe -> binding.recipeCooktime.text = newRecipe.cooktime.toString() })

        // Show the Recipe Description
        recipeViewModel.recipe.observe(
            viewLifecycleOwner,
            Observer { newRecipe -> binding.recipeDescription.text = newRecipe.description })


        // Set the Recipe Image as the Cookbook Image
        if (!recipeDao.getRecipe(recipeId)
                ?.let { cookbookDao.getCookbook(it.cookbookId).uri.isEmpty() }!!
        ) {
            binding.imageView2.setImageURI(Uri.parse(recipeDao.getRecipe(recipeId)?.let {
                cookbookDao.getCookbook(
                    it.cookbookId
                ).uri
            }))
        } else {
            binding.imageView2.setImageResource(R.drawable.chicken)
        }

        // ClickListner for recipe remove
        binding.removeRecipe.setOnClickListener {

            val action = recipeViewModel.recipe.value?.let { it1 ->
                RecipeFragmentDirections.actionRecipeFragmentToCookbookFragment(
                    it1.cookbookId
                )
            }
            recipeViewModel.removeRecipe(recipeId)
            if (action != null) {
                Navigation.findNavController(binding.root).navigate(action)
            }
        }

        return binding.root
    }
}
