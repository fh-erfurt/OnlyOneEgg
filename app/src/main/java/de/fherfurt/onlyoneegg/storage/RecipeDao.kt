package de.fherfurt.onlyoneegg.storage

import androidx.lifecycle.LiveData
import androidx.room.*
import de.fherfurt.onlyoneegg.model.Recipe
import de.fherfurt.onlyoneegg.model.RecipeWithIngredients

/*
* Defines all sql operations needed to manage recipes
* */
@Dao
interface RecipeDao {

    @Insert
    fun insert(recipe: Recipe): Long

    @Update
    suspend fun update(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("DELETE FROM recipe_table where id = :id ")
    fun deleteById(id: Long)

    @Query("DELETE FROM ingredient_table WHERE recipeId = :recipeId")
    fun deleteIngredientsByRecipeId(recipeId: Long)

    @Query("SELECT * FROM recipe_table where id = :id ")
    fun getRecipe(id: Long): Recipe?

    @Transaction
    @Query("SELECT * FROM recipe_table")
    fun getRecipesWithIngredients(): LiveData<List<RecipeWithIngredients>>

    @Query("SELECT * FROM recipe_table ORDER BY id DESC")
    fun getAllRecipes(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe_table WHERE cookbookId= :cookbookId ORDER BY id DESC")
    fun getAllRecipesFromCertainCookbook(cookbookId: Long): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe_table WHERE cookbookId= :cookbookId ORDER BY id DESC")
    fun getAllRecipesFromCertainCookbookList(cookbookId: Long): List<Recipe>


    @Query("SELECT id FROM recipe_table ORDER BY created DESC LIMIT 1")
    fun getLastId(): Long

    // uses SQLite string concatenation for the like
    @Query("SELECT * FROM recipe_table WHERE name LIKE '%' || :keyword || '%' ORDER BY created DESC")
    fun getAllRecipesWithString(keyword: String): List<Recipe>
}

