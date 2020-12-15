package de.fherfurt.onlyoneegg.storage

import androidx.lifecycle.LiveData
import androidx.room.*
import de.fherfurt.onlyoneegg.model.Recipe
import de.fherfurt.onlyoneegg.model.RecipeWithIngredients


@Dao
interface RecipeDao {

    @Insert
    fun insert(recipe: Recipe): Long

    @Update
    suspend fun update(recipe: Recipe)

    @Query("SELECT * FROM recipe_table where id = :id ")
    suspend fun getRecipe(id : Int): Recipe?

    @Transaction
    @Query("SELECT * FROM recipe_table")
    fun getRecipesWithIngredients(): LiveData<List<RecipeWithIngredients>>

    @Query("SELECT * FROM recipe_table ORDER BY id DESC")
    fun getAllRecipes(): LiveData<List<Recipe>>

    @Query("SELECT id FROM recipe_table ORDER BY created DESC LIMIT 1")
    fun getLastId(): Long
}

