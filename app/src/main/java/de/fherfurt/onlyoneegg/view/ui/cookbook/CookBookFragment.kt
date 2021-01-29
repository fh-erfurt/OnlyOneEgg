package de.fherfurt.onlyoneegg.view.ui.cookbook

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.`interface`.ViewModelCustom
import de.fherfurt.onlyoneegg.databinding.FragmentCookbookBinding
import de.fherfurt.onlyoneegg.model.ExportRecipe
import de.fherfurt.onlyoneegg.model.Ingredient
import de.fherfurt.onlyoneegg.model.Recipe
import de.fherfurt.onlyoneegg.storage.CookbookRepository
import de.fherfurt.onlyoneegg.storage.IngredientRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import de.fherfurt.onlyoneegg.storage.RecipeRepository

/*
* Cookbook Recycle View implementation class
* is used to list all recipes of the certain cookbook
*
* */
class CookBookFragment : Fragment(){

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // set the Fragment as only Portrait
        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //initialize a cookbookId from nav args
        val args: CookBookFragmentArgs by navArgs()
        val cookbookId = args.cookbookId


        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentCookbookBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_cookbook, container, false
        )

        val application = requireNotNull(this.activity).application
        val ingredientDao = OOEDatabase.getInstance(application).ingredientDao
        val recipeDao = OOEDatabase.getInstance(application).recipeDao
        val cookbookDao = OOEDatabase.getInstance(application).cookbookDao

        val recipeRepository = RecipeRepository(recipeDao)
        val ingredientRepository = IngredientRepository(ingredientDao)
        val cookbookRepository = CookbookRepository(cookbookDao)
        val viewModelFactory =
            CookBookViewModelFactory(application, recipeRepository, cookbookRepository, cookbookId)

        val cookbookViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(CookBookViewModel::class.java)
        // click listener to navigate to the addRecipeFragment with cookbookId as argument
        binding.addRecipe.setOnClickListener {

            val action =
                CookBookFragmentDirections.actionCookbookFragmentToAddRecipeFragment2(cookbookId)
            Navigation.findNavController(binding.root).navigate(action)

        }
        // click listener for removing of entire cookbook
        binding.removeCookbook.setOnClickListener {
            cookbookViewModel.removeCookbook(cookbookId, recipeRepository, cookbookRepository);
            findNavController().navigate(R.id.action_cookbookFragment_to_dashboardFragment)
        }

        binding.exportRecipes.setOnClickListener {
            var recipes: List<Recipe> = cookbookViewModel.recipeRepository.getAllRecipesFromCertainCookbookList(cookbookId);

            //ingredientRepository.getAllIngredients(recipes[0].id)

            var recipeList = ArrayList<ExportRecipe>()

           // println( ingredientRepository.getAllIngredientsAsList(recipes[0].id))
            recipes.forEach{
                val exportRecipe : ExportRecipe = ExportRecipe( it,ingredientRepository.getAllIngredientsFromCertainRecipeList(it.id ))
                //println(exportRecipe)
                recipeList.add(exportRecipe)

            }
            println(recipeList)
        }



        binding.cookbookViewModel = cookbookViewModel
        /** binding.addRecipe.setOnClickListener { findNavController().navigate(R.id.action_cookbookFragment_to_inputrecipeFragment) } **/
        binding.setLifecycleOwner(this)

        val adapter = CookBookAdapter()
        binding.recipeList.adapter = adapter

        cookbookViewModel.recipes.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })
        val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        binding.recipeList.layoutManager = manager
        return binding.root
    }


}
