package de.fherfurt.onlyoneegg.view.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.databinding.FragmentRecipeBinding
import de.fherfurt.onlyoneegg.storage.IngredientRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import de.fherfurt.onlyoneegg.storage.RecipeRepository


class RecipeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding : FragmentRecipeBinding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_recipe, container, false)


        val application = requireNotNull(this.activity).application
        val recipeDao = OOEDatabase.getInstance(application).recipeDao;

        val ingredientDao = OOEDatabase.getInstance(application).ingredientDao;
        val ingredientRepository = IngredientRepository(ingredientDao)
        val viewModelFactory = RecipeViewModelFactory(application,ingredientRepository)

        val recipeViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(RecipeViewModel::class.java)

        binding.lifecycleOwner = this

        binding.recipeViewModel = recipeViewModel

        val adapter = RecipeAdapter()
        binding.ingredientList.adapter = adapter

        recipeViewModel.ingredients.observe(viewLifecycleOwner, {
            it?.let{
                adapter.submitList(it)
            }
        })
        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        binding.ingredientList.layoutManager = manager


        recipeViewModel.recipe.observe(viewLifecycleOwner, Observer { newRecipe ->  binding.recipeName.text = newRecipe.name })

        recipeViewModel.recipe.observe(viewLifecycleOwner, Observer { newRecipe ->  binding.recipeDescription.text = newRecipe.description })




        return binding.root


    }

}
//recipeViewModel.recipe.observe(viewLifecycleOwner, Observer { newRecipe -> Log.i("RecipeFragment", newRecipe )   })
