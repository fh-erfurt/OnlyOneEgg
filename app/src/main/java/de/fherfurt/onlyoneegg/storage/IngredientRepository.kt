package de.fherfurt.onlyoneegg.storage

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import de.fherfurt.onlyoneegg.model.Ingredient

/*
* Uses IngredientDao and manages the access to it
* */
class IngredientRepository(private val ingredientDao: IngredientDao) {

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
     fun insert(ingredient: Ingredient) {
        ingredientDao.insert(ingredient)
    }

    suspend fun insertArrayList(ingredients: ArrayList<Ingredient>) {
        ingredientDao.insertArrayList(ingredients)
    }
    
    fun getAllIngredients(recipeId : Long): LiveData<List<Ingredient>> {
        return ingredientDao.getAllIngredientsFromRecipe(recipeId )
    }

    fun getAllIngredientsFromCertainRecipeList(recipeId : Long): List<Ingredient>{
        return ingredientDao.getAllIngredientsFromCertainRecipeList(recipeId )
    }

}
