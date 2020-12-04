package de.fherfurt.onlyoneegg.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import de.fherfurt.onlyoneegg.model.Ingredient
import de.fherfurt.onlyoneegg.model.Recipe


@Dao
interface RecipeDao {

    @Insert
    suspend fun insert(recipe: Recipe)


    @Query("SELECT * FROM recipe_table ORDER BY id ASC")
    suspend fun getRecipe(): Recipe?


}

