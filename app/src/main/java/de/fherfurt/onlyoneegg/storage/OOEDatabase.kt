package de.fherfurt.onlyoneegg.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.fherfurt.onlyoneegg.model.Cookbook
import de.fherfurt.onlyoneegg.model.Ingredient
import de.fherfurt.onlyoneegg.model.Recipe

@Database(entities = [Ingredient::class,Recipe::class,Cookbook::class], version = 23, exportSchema = false)
@TypeConverters(Converters::class)

abstract class OOEDatabase : RoomDatabase() {
    abstract val ingredientDao: IngredientDao
    abstract val recipeDao : RecipeDao
    abstract val cookbookDao: CookbookDao

    companion object {
        val LOG_TAG_DB: String = "OOEDatabase"

        @Volatile
        private var INSTANCE: OOEDatabase? = null

        fun getInstance(context: Context): OOEDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        OOEDatabase::class.java,
                        "OOEDatabase"
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}