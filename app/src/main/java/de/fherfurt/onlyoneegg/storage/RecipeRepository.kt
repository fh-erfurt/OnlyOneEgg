package de.fherfurt.onlyoneegg.storage

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import de.fherfurt.onlyoneegg.model.Recipe

class RecipeRepository(private val recipeDao: RecipeDao) {


    fun insert(recipe: Recipe): Long {
        return recipeDao.insert(recipe)
    }
    fun getAllRecipesFromCertainCookbook(cookbookId: Long):LiveData<List<Recipe>>{
        return recipeDao.getAllRecipesFromCertainCookbook(cookbookId)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun get(id: Long) {
        recipeDao.getRecipe(id)
    }

    fun getAllRecipes(): LiveData<List<Recipe>> {
        return recipeDao.getAllRecipes()
    }

    suspend fun deleteRecipeWithIngredients(id: Long){
        recipeDao.deleteIngredientsByRecipeId(id)
        recipeDao.deleteById(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getLastId(): Long {
        return recipeDao.getLastId()
    }
}