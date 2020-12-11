package de.fherfurt.onlyoneegg.view.ui.recipe

import android.app.Application
import android.util.Log

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import de.fherfurt.onlyoneegg.model.Recipe
import de.fherfurt.onlyoneegg.model.RecipeWithIngredients
import de.fherfurt.onlyoneegg.storage.IngredientRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import de.fherfurt.onlyoneegg.storage.RecipeRepository
import kotlinx.coroutines.launch

class RecipeViewModel (application: Application, ingredientRepository: IngredientRepository): AndroidViewModel(application) {


    var recipe = MutableLiveData<Recipe>()

    val recipeDao = OOEDatabase.getInstance(application).recipeDao;

    val ingredients  = ingredientRepository.getAllIngredients(1)

    init{
        initializeRecipe(1)

    }

    private fun initializeRecipe(id : Int){

        viewModelScope.launch {
            recipe.value = getRecipeFromDatabase(id)
        }
    }


    fun onInsertRecipe(){
        val recipeTest = Recipe()
        recipeTest.name = "Checken"
        recipeTest.description = "MiauMiau"
        recipeTest.cooktime = 23.4f
        recipeTest.difficulty = "Hard"
        viewModelScope.launch {
            val checken = recipeDao.insert(recipeTest) ?: return@launch
        }

    }

    private suspend fun getRecipeFromDatabase(id : Int) : Recipe? {
        var recipe = recipeDao.getRecipe(id)
        if(recipe == null)
            recipe = null

        return recipe
    }




}

