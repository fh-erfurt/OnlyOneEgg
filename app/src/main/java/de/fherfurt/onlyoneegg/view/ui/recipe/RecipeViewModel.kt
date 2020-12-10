package de.fherfurt.onlyoneegg.view.ui.recipe

import android.app.Application
import android.util.Log

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import de.fherfurt.onlyoneegg.model.Recipe
import de.fherfurt.onlyoneegg.model.RecipeWithIngredients
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import de.fherfurt.onlyoneegg.storage.RecipeRepository
import kotlinx.coroutines.launch

class RecipeViewModel (application: Application): AndroidViewModel(application) {


     var recipe = MutableLiveData<String>()


    val recipeDao = OOEDatabase.getInstance(application).recipeDao;

    //val recipes : List<RecipeWithIngredients> = recipeDao.getRecipesWithIngredients()

    val recipeName ="Greek Chicken Skewers"

    val recipeDescription ="Whisk lemon juice, oil, vinegar, onion flakes, garlic, lemon zest, Greek seasoning," +
            " poultry seasoning, oregano, pepper, and thyme together in a bowl and pour into a resealable plastic bag."

    init{
        initializeRecipe()

    }

    private fun initializeRecipe(){
        // var recipe = MutableLiveData<Recipe?>()
        viewModelScope.launch {
            recipe.value = getRecipeFromDatabase()?.name
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

    private suspend fun getRecipeFromDatabase() : Recipe?{
        var recipe = recipeDao.getRecipe()
       /* if(recipe?.description == null)
            recipe=null*/
        if (recipe != null) {
         //   Log.i("RecipeViewModel",recipe.description)
        }
       // Log.i("RecipeViewModel","test")

        return recipe
    }




}

