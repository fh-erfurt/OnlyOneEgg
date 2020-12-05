package de.fherfurt.onlyoneegg.view.ui.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.fherfurt.onlyoneegg.model.Recipe
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import de.fherfurt.onlyoneegg.storage.RecipeRepository
import kotlinx.coroutines.launch

class RecipeViewModel (application: Application): AndroidViewModel(application) {

    val recipeTest = Recipe()


    val recipeDao = OOEDatabase.getInstance(application).recipeDao;

    val recipeName ="Greek Chicken Skewers"

    val recipe ="Whisk lemon juice, oil, vinegar, onion flakes, garlic, lemon zest, Greek seasoning," +
            " poultry seasoning, oregano, pepper, and thyme together in a bowl and pour into a resealable plastic bag."


    fun onInsertIngredient(){
        recipeTest.name = "Checken"
        recipeTest.description = "MiauMiau"
        recipeTest.cooktime = 23.4f
        recipeTest.difficulty = "Hard"
        viewModelScope.launch {
            val checken = recipeDao.insert(recipeTest) ?: return@launch
        }
    }

}