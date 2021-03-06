package de.fherfurt.onlyoneegg.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
* Enum for saving all available measurements
* */
enum class Measurement() {
    l,
    ml,
    g,
    pc,
}

/*
* Entity class Ingredient stores needed values for the usage of it
* */
@Entity(tableName = "ingredient_table")
data class Ingredient(
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
    @ColumnInfo(name = "measurement")
    var measurement: Measurement = Measurement.pc,

    @NonNull
    @ColumnInfo(name = "value")
    var value: Long = -1,

    @NonNull
    @ColumnInfo(name = "recipeId")
    var recipeId: Long = 1,
)