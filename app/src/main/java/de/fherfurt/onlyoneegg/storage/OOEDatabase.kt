package de.fherfurt.onlyoneegg.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.fherfurt.onlyoneegg.model.Ingredient


@Database(entities = [Ingredient::class], version = 3, exportSchema = false)
abstract class OOEDatabase : RoomDatabase() {
    abstract val ingredientDao: IngredientDao

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
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}