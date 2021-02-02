package de.fherfurt.onlyoneegg.storage

import androidx.lifecycle.LiveData
import androidx.room.*
import de.fherfurt.onlyoneegg.model.Cookbook

/*
* Defines all sql operations needed to manage a cookbook
*
* */
@Dao
interface CookbookDao {

    @Insert
    suspend fun insert(cookbook: Cookbook)
    @Update
    suspend fun update(cookbook: Cookbook)
    @Delete
     fun delete(cookbook: Cookbook)

     // returns first cookbook from the database
    @Query("SELECT * FROM cookbook_table ORDER BY id DESC LIMIT 1")
    fun getCookbook(): Cookbook
    // returns a cookbook with a certain id
    @Query("SELECT * FROM cookbook_table WHERE id=:id LIMIT 1")
    fun getCookbook(id:Long): Cookbook
    // returns a last cookbook
    @Query("SELECT id FROM cookbook_table ORDER BY id DESC LIMIT 1")
    fun lastCookbookId(): Long
    // returns a all cookbooks from the database as list of live data
    @Query("SELECT * FROM cookbook_table ORDER BY id DESC")
    fun getAllCookbooks(): LiveData<List<Cookbook>>




}
