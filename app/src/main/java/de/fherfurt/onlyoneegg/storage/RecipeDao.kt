package de.fherfurt.onlyoneegg.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import de.fherfurt.onlyoneegg.model.Ingredient
import de.fherfurt.onlyoneegg.model.Recipe
import de.fherfurt.onlyoneegg.model.RecipeWithIngredients


@Dao
interface RecipeDao {

    @Insert
    suspend fun insert(recipe: Recipe)


    @Query("SELECT * FROM recipe_table ORDER BY recipeId ASC")
    suspend fun getRecipe(): Recipe?

    @Transaction
    @Query("SELECT * FROM recipe_table")
    fun getRecipesWithIngredients(): List<RecipeWithIngredients>



}

