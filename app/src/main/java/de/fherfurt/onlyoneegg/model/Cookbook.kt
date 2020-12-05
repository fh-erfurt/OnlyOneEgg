package de.fherfurt.onlyoneegg.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey




@Entity(tableName = "cookbook_table")
data class Cookbook(

    @PrimaryKey(autoGenerate = true)
    var cookbookId: Long = 0L,

    @ColumnInfo(name = "created")
    val created: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated")
    var updated: Long = created,

    @NonNull
    @ColumnInfo(name = "cookbookName")
    var cookbookName: String = "",

    )

