package de.fherfurt.onlyoneegg.view.ui.cookbook

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.model.Recipe
import de.fherfurt.onlyoneegg.storage.CookbookRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import de.fherfurt.onlyoneegg.storage.RecipeRepository
import kotlinx.coroutines.launch



class CookBookViewModel (application: Application, recipeRepository: RecipeRepository,
                         cookbookRepository: CookbookRepository,cookbookId:Long): AndroidViewModel(application) {

    val recipeRepository = recipeRepository
    val recipes = recipeRepository.getAllRecipesFromCertainCookbook(cookbookId)
    val cookbookRepository=cookbookRepository

    fun removeCookbook(cookbookId: Long){
        var it= recipeRepository.getAllRecipesFromCertainCookbookList(cookbookId).iterator()
        // delete all recipes of the certain cookbook
        while (it.hasNext()) {
            recipeRepository.deleteRecipeWithIngredients(it.next().id)
        }
        //delete cookbook
        cookbookRepository.delete(cookbookId)

    }

}
