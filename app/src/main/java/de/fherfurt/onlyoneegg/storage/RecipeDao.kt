package de.fherfurt.onlyoneegg.storage

import androidx.lifecycle.LiveData
import androidx.room.*
import de.fherfurt.onlyoneegg.model.Cookbook
import de.fherfurt.onlyoneegg.model.Ingredient
import de.fherfurt.onlyoneegg.model.Recipe
import de.fherfurt.onlyoneegg.model.RecipeWithIngredients


@Dao
interface RecipeDao {

    @Insert
    suspend fun insert(recipe: Recipe)

    @Update
    suspend fun update(recipe: Recipe)

    @Query("SELECT * FROM recipe_table where recipeId = :id ")
    suspend fun getRecipe(id : Int): Recipe?

    @Transaction
    @Query("SELECT * FROM recipe_table")
    fun getRecipesWithIngredients(): LiveData<List<RecipeWithIngredients>>


    @Query("SELECT * FROM recipe_table ORDER BY recipeId DESC")
    fun getAllRecipes(): LiveData<List<Recipe>>


}

