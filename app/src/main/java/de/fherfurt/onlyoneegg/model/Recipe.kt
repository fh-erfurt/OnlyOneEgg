package de.fherfurt.onlyoneegg.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


enum class Difficulty() {
        easy,
        middle,
        hard,
        complex,
}


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
        // TODO make difficulty an enum
        var difficulty: Difficulty = Difficulty.easy,

        @NonNull
        @ColumnInfo(name = "cookbookId")
        var myCookbookId: Long = 1,
)
