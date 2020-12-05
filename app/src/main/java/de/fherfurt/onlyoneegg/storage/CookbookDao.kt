package de.fherfurt.onlyoneegg.storage

import androidx.lifecycle.LiveData
import androidx.room.*
import de.fherfurt.onlyoneegg.model.Cookbook


@Dao
interface CookbookDao {

    @Insert
    suspend fun insert(cookbook: Cookbook)
    @Update
    suspend fun update(cookbook: Cookbook)
    @Delete
    suspend fun delete(cookbook: Cookbook)

    @Query("SELECT * FROM cookbook_table ORDER BY cookbookId DESC LIMIT 1")
    suspend fun getCookbook(): Cookbook?

 /*@Query("SELECT * FROM cookbook_table ORDER BY id DESC")
    fun getAllNights(): LiveData<List<Cookbook>>*/


   /* @Transaction
    @Query("SELECT * FROM cookbook_table")
    fun getCookbookWithRecipes(): List<CookbookWithRecipes>*/

}