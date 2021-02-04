package de.fherfurt.onlyoneegg.interfaceCustom

import de.fherfurt.onlyoneegg.storage.CookbookRepository
import de.fherfurt.onlyoneegg.storage.RecipeRepository

/*
* interface to summarize duplicate code in cookbook view model and dashboard view model classes
*
* */
interface ViewModelCustom {

    /*
    * removes cookbook with all recipes and ingredients from database
    * */
    fun removeCookbook(
        cookbookId: Long,
        recipeRepository: RecipeRepository,
        cookbookRepository: CookbookRepository
    ) {
        val it = recipeRepository.getAllRecipesFromCertainCookbookList(cookbookId).iterator()
        // delete all recipes of the certain cookbook
        while (it.hasNext()) {
            recipeRepository.deleteRecipeWithIngredients(it.next().id)
        }
        //delete cookbook
        cookbookRepository.delete(cookbookId)
    }
}