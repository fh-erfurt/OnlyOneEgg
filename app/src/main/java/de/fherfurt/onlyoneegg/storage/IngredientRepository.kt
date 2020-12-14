package de.fherfurt.onlyoneegg.storage

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import de.fherfurt.onlyoneegg.model.Ingredient

class IngredientRepository(private val ingredientDao: IngredientDao) {
    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(ingredient: Ingredient) {
        ingredientDao.insert(ingredient)
    }

    suspend fun insertArrayList(ingredients: ArrayList<Ingredient>) {
        ingredientDao.insertArrayList(ingredients)
    }

    fun getAllIngredients(recipeId : Int): LiveData<List<Ingredient>> {
        return ingredientDao.getAllIngredientsFromRecipe(recipeId )
    }

}