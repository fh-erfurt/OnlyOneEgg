package de.fherfurt.onlyoneegg.storage

import androidx.room.TypeConverter
import de.fherfurt.onlyoneegg.model.Difficulty
import de.fherfurt.onlyoneegg.model.Measurement
/*
* Custom Converters used by the database
* All of them are converting enums to string
* */
class Converters {

    @TypeConverter
    fun toMeasurement(value: String) = enumValueOf<Measurement>(value)

    @TypeConverter
    fun fromMeasurement(value: Measurement) = value.name

    @TypeConverter
    fun toDifficulty(value: String) = enumValueOf<Difficulty>(value)

    @TypeConverter
    fun fromDifficulty(value: Difficulty) = value.name
}