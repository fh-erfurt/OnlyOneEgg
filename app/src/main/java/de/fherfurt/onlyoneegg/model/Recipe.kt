package de.fherfurt.onlyoneegg.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
* Enum for defining the difficulty of a recipe
* */
enum class Difficulty() {
        easy,
        middle,
        hard,
        complex,
}

/*
* Entity class Recipe stores needed values like name, description etc.
* */
@Entity(tableName = "recipe_table")
data class Recipe(


        @PrimaryKey(autoGenerate = true)
        var id: Long = 0L,

        @ColumnInfo(name = "created")
        val created: Long = System.currentTimeMillis(),

        @ColumnInfo(name = "updated")
        var updated: Long = created,

        @NonNull
        @ColumnInfo(name = "name")
        var name: String = "",


        @NonNull
        @ColumnInfo(name = "description")
        var description: String = "",

        @NonNull
        @ColumnInfo(name = "cooktime")
        var cooktime: Float = 0F,

        @NonNull
        @ColumnInfo(name = "difficulty")
        var difficulty: Difficulty = Difficulty.easy,

        @NonNull
        @ColumnInfo(name = "cookbookId")
        var cookbookId: Long = 1,
)
