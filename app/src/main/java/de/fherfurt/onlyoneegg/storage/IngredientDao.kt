package de.fherfurt.onlyoneegg.storage

import androidx.lifecycle.LiveData
import androidx.room.*
import de.fherfurt.onlyoneegg.model.Ingredient

/*
* Defines all sql operations needed to manage ingredients
* */
@Dao
interface IngredientDao {
    @Insert
     fun insert(ingredient: Ingredient)

    @Insert
    suspend fun insertArrayList(ingredients: ArrayList<Ingredient>)

    @Update
    suspend fun update(ingredient: Ingredient)

    @Delete
    suspend fun delete(ingredient: Ingredient)

    @Query("SELECT * FROM ingredient_table ORDER BY id DESC LIMIT 1")
    suspend fun getIngredient(): Ingredient?

    @Query("SELECT * from ingredient_table ORDER BY id ASC")
    fun getAllIngredients(): LiveData<List<Ingredient>>

    @Query("SELECT * from ingredient_table where recipeId = :recipeId ORDER BY id ASC")
    fun getAllIngredientsFromRecipe( recipeId: Long): LiveData<List<Ingredient>>

    @Query("SELECT * from ingredient_table where recipeId = :recipeId ORDER BY id ASC")
    fun getAllIngredientsFromCertainRecipeList(recipeId: Long): List<Ingredient>

}