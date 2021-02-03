package de.fherfurt.onlyoneegg.view.ui.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.fherfurt.onlyoneegg.model.Recipe
import de.fherfurt.onlyoneegg.storage.IngredientRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import de.fherfurt.onlyoneegg.storage.RecipeRepository
import kotlinx.coroutines.launch

/*
* RecipeViewModel class fetches the recipes with their respective ingredients from the database
* */
class RecipeViewModel(
    application: Application,
    ingredientRepository: IngredientRepository,
    recipeID: Long
) : AndroidViewModel(application) {


    var recipe = MutableLiveData<Recipe>()

    val recipeDao = OOEDatabase.getInstance(application).recipeDao;
    val recipeRepository = RecipeRepository(recipeDao);
    val ingredients = ingredientRepository.getAllIngredients(recipeID)

    init {
        initializeRecipe(recipeID)

    }

    // Initialize a recipe
    private fun initializeRecipe(id: Long) {
        viewModelScope.launch {
            recipe.value = getRecipeFromDatabase(id)
        }
    }

    // fetch the Recipe with a Specific ID from the Database
    private suspend fun getRecipeFromDatabase(id: Long): Recipe? {
        var recipe = recipeDao.getRecipe(id)
        if (recipe == null)
            recipe = null
        return recipe
    }


    fun removeRecipe(recipeId: Long) {
        recipeRepository.deleteRecipeWithIngredients(recipeId)
    }


}

