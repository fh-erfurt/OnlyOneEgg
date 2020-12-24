package de.fherfurt.onlyoneegg.view.ui.cookbook

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.fherfurt.onlyoneegg.model.Recipe
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import de.fherfurt.onlyoneegg.storage.RecipeRepository
import kotlinx.coroutines.launch



class CookBookViewModel (application: Application, recipeRepository: RecipeRepository, cookbookId:Long): AndroidViewModel(application) {

    val recipeRepository = recipeRepository
    val recipes = recipeRepository.getAllRecipesFromCertainCookbook(cookbookId)

    val recipeTest = Recipe()
    val recipeDao = OOEDatabase.getInstance(application).recipeDao;

    fun onClick() {
        recipeTest.name = "Chicken"

        viewModelScope.launch {
            val chicken = recipeDao.insert(recipeTest) ?: return@launch
            val fish = Recipe()
            fish.name = "Fish"
            recipeDao.insert(fish)
        }

    }

}
