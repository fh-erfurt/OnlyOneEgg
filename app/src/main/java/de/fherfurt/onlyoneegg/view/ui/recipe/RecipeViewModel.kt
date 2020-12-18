package de.fherfurt.onlyoneegg.view.ui.recipe

import android.app.Application
import android.util.Log

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs

import de.fherfurt.onlyoneegg.model.Recipe
import de.fherfurt.onlyoneegg.model.RecipeWithIngredients
import de.fherfurt.onlyoneegg.storage.IngredientRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import de.fherfurt.onlyoneegg.storage.RecipeRepository
import kotlinx.coroutines.launch

class RecipeViewModel (application: Application, ingredientRepository: IngredientRepository, recipeID : Long ): AndroidViewModel(application) {


    var recipe = MutableLiveData<Recipe>()

    val recipeDao = OOEDatabase.getInstance(application).recipeDao;

    val ingredients  = ingredientRepository.getAllIngredients(recipeID)

    init{
        initializeRecipe(recipeID)

    }

    private fun initializeRecipe(id : Long){

        viewModelScope.launch {
            recipe.value = getRecipeFromDatabase(id)
        }
    }


    private suspend fun getRecipeFromDatabase(id : Long) : Recipe? {
        var recipe = recipeDao.getRecipe(id)
        if(recipe == null)
            recipe = null

        return recipe
    }




}

