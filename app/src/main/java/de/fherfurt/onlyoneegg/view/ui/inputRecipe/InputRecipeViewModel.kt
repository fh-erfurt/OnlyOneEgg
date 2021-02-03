package de.fherfurt.onlyoneegg.view.ui.inputRecipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.fherfurt.onlyoneegg.model.Ingredient
import de.fherfurt.onlyoneegg.model.Recipe
import de.fherfurt.onlyoneegg.storage.IngredientRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import de.fherfurt.onlyoneegg.storage.RecipeRepository
import kotlinx.coroutines.launch

/*
* AddRecipeViewModel class uses the recipe/ingredient repository
* */
class InputRecipeViewModel(application: Application) : AndroidViewModel(application) {
    val recipeDao = OOEDatabase.getInstance(application).recipeDao;
    val recipeRepository = RecipeRepository(recipeDao)

    val ingredientDao = OOEDatabase.getInstance(application).ingredientDao;
    val ingredientRepository = IngredientRepository(ingredientDao)

    fun insertRecipe(recipe: Recipe): Long {
        return recipeRepository.insert(recipe)
    }

    fun insertIngredientList(ingredients: ArrayList<Ingredient>) {
        viewModelScope.launch {
            val id = ingredientRepository.insertArrayList(ingredients)
        }
    }
}