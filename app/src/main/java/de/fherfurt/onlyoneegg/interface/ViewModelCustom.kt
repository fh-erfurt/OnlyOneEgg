package de.fherfurt.onlyoneegg.`interface`

import android.app.Application
import de.fherfurt.onlyoneegg.storage.CookbookRepository
import de.fherfurt.onlyoneegg.storage.RecipeRepository

interface ViewModelCustom{

    /*fun removeCookbook(cookbookId: Long, recipeRepository: RecipeRepository, cookbookRepository: CookbookRepository){
        var it= recipeRepository.getAllRecipesFromCertainCookbookList(cookbookId).iterator()
        // delete all recipes of the certain cookbook
        while (it.hasNext()) {
            recipeRepository.deleteRecipeWithIngredients(it.next().id)
        }
        //delete cookbook
        cookbookRepository.delete(cookbookId)

    }*/
}