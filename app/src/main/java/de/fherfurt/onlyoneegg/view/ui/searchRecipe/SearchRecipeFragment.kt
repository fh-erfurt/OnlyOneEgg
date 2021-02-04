package de.fherfurt.onlyoneegg.view.ui.searchRecipe

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.databinding.FragmentSearchRecipeBinding
import de.fherfurt.onlyoneegg.model.Recipe
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import de.fherfurt.onlyoneegg.storage.RecipeRepository

/*
* Fragment for searching for a recipe in all cookbooks and listing all the ones found
* */
class SearchRecipeFragment : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // set the Fragment as only Portrait
        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSearchRecipeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search_recipe, container, false
        )

        val application = requireNotNull(this.activity).application
        val recipeDao = OOEDatabase.getInstance(application).recipeDao
        val recipeRepository = RecipeRepository(recipeDao)

        binding.lifecycleOwner = this

        // setup recipeList for the found recipes
        val recipes = ArrayList<Recipe>()
        val adapter = SearchRecipeAdapter(recipes)
        binding.recipeList.adapter = adapter
        binding.recipeList.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)

        // search for all recipes which include the input from the user
        binding.buttonSearch.setOnClickListener {
            recipes.clear()
            val foundRecipes =
                recipeRepository.getAllRecipesWithString(binding.inputSearchText.text.toString())
            // add found recipes to the recyclerView else give the user info that none were found
            if (foundRecipes.isNotEmpty()) {
                binding.emptyText.text = ""
                foundRecipes.forEach {
                    recipes.add(it)
                }
            } else {
                binding.emptyText.text = "No recipes found"
            }
            adapter.notifyDataSetChanged()
        }

        val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        binding.recipeList.layoutManager = manager

        return binding.root
    }

}