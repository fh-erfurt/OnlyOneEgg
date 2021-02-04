package de.fherfurt.onlyoneegg.storage

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import de.fherfurt.onlyoneegg.model.Recipe

/*
* Uses RecipeDao and manages the access to it
* */
class RecipeRepository(private val recipeDao: RecipeDao) {

    fun insert(recipe: Recipe): Long {
        return recipeDao.insert(recipe)
    }

    fun getAllRecipesFromCertainCookbook(cookbookId: Long): LiveData<List<Recipe>> {
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

    fun deleteRecipeWithIngredients(id: Long) {
        recipeDao.deleteIngredientsByRecipeId(id)
        recipeDao.deleteById(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getLastId(): Long {
        return recipeDao.getLastId()
    }

    fun getRecipe(recipeId: Long): Recipe? {
        return recipeDao.getRecipe(recipeId)
    }

    fun getAllRecipesFromCertainCookbookList(cookbookId: Long): List<Recipe> {
        return recipeDao.getAllRecipesFromCertainCookbookList(cookbookId)
    }

    fun getAllRecipesWithString(keyword: String): List<Recipe> {
        return if (keyword.trim().isBlank()) {
            ArrayList<Recipe>()
        } else {
            recipeDao.getAllRecipesWithString(keyword.trim())
        }
    }
}