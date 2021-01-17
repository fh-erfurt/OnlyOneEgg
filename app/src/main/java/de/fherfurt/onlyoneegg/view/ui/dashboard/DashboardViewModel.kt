package de.fherfurt.onlyoneegg.view.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.fherfurt.onlyoneegg.`interface`.ViewModelCustom
import de.fherfurt.onlyoneegg.model.Cookbook
import de.fherfurt.onlyoneegg.storage.CookbookRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import de.fherfurt.onlyoneegg.storage.RecipeRepository
import kotlinx.coroutines.launch

/*
* DashboardViewModel class tracks all cookbooks and uses cookbookRepository
* */
class DashboardViewModel(application: Application, cookbookRepository: CookbookRepository,
                         recipeRepository: RecipeRepository): AndroidViewModel(application){

    val cookbookRepository=cookbookRepository
    val recipeRepository=recipeRepository
    val cookbooks=cookbookRepository.getAllCookbooks()

    fun removeAllSelectedCookbooks(ids: MutableList<Long>){
        ids.forEach { id->
            removeCookbook(id, recipeRepository, cookbookRepository);

        }
    }
    fun removeCookbook(cookbookId: Long, recipeRepository: RecipeRepository, cookbookRepository: CookbookRepository){
        var it= recipeRepository.getAllRecipesFromCertainCookbookList(cookbookId).iterator()
        // delete all recipes of the certain cookbook
        while (it.hasNext()) {
            recipeRepository.deleteRecipeWithIngredients(it.next().id)
        }
        //delete cookbook
        cookbookRepository.delete(cookbookId)

    }

}