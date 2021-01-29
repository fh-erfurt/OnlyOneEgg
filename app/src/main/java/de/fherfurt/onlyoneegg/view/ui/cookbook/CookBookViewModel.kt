package de.fherfurt.onlyoneegg.view.ui.cookbook

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.fherfurt.onlyoneegg.interfaceCustom.ViewModelCustom
import de.fherfurt.onlyoneegg.storage.CookbookRepository
import de.fherfurt.onlyoneegg.storage.RecipeRepository

/*
* CookBookViewModel class tracks all recipes and uses RecipeRepository
* */
class CookBookViewModel(
    application: Application, recipeRepository: RecipeRepository,
    cookbookRepository: CookbookRepository, cookbookId: Long
) : AndroidViewModel(application), ViewModelCustom {

    val recipeRepository = recipeRepository
    val recipes = recipeRepository.getAllRecipesFromCertainCookbook(cookbookId)



    fun removeAllSelectedRecipes(ids: MutableList<Long>){
        ids.forEach { id->
            recipeRepository.deleteRecipeWithIngredients(id);
        }
    }
}
