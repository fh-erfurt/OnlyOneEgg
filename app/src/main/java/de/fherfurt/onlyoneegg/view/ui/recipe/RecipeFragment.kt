package de.fherfurt.onlyoneegg.view.ui.recipe

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.databinding.FragmentRecipeBinding
import de.fherfurt.onlyoneegg.model.StorageUtils
import de.fherfurt.onlyoneegg.storage.IngredientRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import org.json.JSONObject


/*
* Recipe Fragment shows the recipe view with its ingredients
* */
class RecipeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //initialize a recipeId from nav args
        val args : RecipeFragmentArgs by navArgs()
        val recipeId = args.recipeId

        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        // Get a reference to the binding object and inflate the fragment views.
        val binding : FragmentRecipeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_recipe, container, false
        )
      val application = requireNotNull(this.activity).application
      
      val recipeDao = OOEDatabase.getInstance(application).recipeDao;
       
      val ingredientDao = OOEDatabase.getInstance(application).ingredientDao;
       
      val ingredientRepository = IngredientRepository(ingredientDao)
      
      val recipeViewModel =RecipeViewModel(application, ingredientRepository, recipeId )


        binding.lifecycleOwner = this

        binding.recipeViewModel = recipeViewModel

        val adapter = RecipeAdapter()
        binding.ingredientList.adapter = adapter



        recipeViewModel.ingredients.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        binding.ingredientList.layoutManager = manager



        recipeViewModel.recipe.observe(
            viewLifecycleOwner,
            Observer { newRecipe -> binding.recipeName.text = newRecipe.name })

        recipeViewModel.recipe.observe(
            viewLifecycleOwner,
            Observer { newRecipe -> binding.recipeDescription.text = newRecipe.description })


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
