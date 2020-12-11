package de.fherfurt.onlyoneegg.storage

import androidx.lifecycle.LiveData
import androidx.room.*
import de.fherfurt.onlyoneegg.model.Ingredient

@Dao
interface IngredientDao {
    @Insert
    suspend fun insert(ingredient: Ingredient)

    @Update
    suspend fun update(ingredient: Ingredient)

    @Delete
    suspend fun delete(ingredient: Ingredient)

    @Query("SELECT * FROM ingredient_table ORDER BY id DESC LIMIT 1")
    suspend fun getIngredient(): Ingredient?

    @Query("SELECT * from ingredient_table ORDER BY id ASC")
     fun getAllIngredients(): LiveData<List<Ingredient>>

}