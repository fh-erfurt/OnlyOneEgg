package de.fherfurt.onlyoneegg.storage

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import de.fherfurt.onlyoneegg.model.Ingredient
import de.fherfurt.onlyoneegg.model.Recipe

class RecipeRepository(private  val recipeDao: RecipeDao) {


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(recipe: Recipe) {
        recipeDao.insert(recipe)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun get(recipe: Recipe) {
        recipeDao.getRecipe()
    }

    fun getAllRecipes(): LiveData<List<Recipe>> {
        return recipeDao.getAllRecipes()
    }

}