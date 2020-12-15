package de.fherfurt.onlyoneegg.storage

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import de.fherfurt.onlyoneegg.model.Recipe

class RecipeRepository(private val recipeDao: RecipeDao) {


    fun insert(recipe: Recipe): Long {
        return recipeDao.insert(recipe)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun get(id: Int) {
        recipeDao.getRecipe(id)
    }

    fun getAllRecipes(): LiveData<List<Recipe>> {
        return recipeDao.getAllRecipes()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getLastId(): Long {
        return recipeDao.getLastId()
    }
}