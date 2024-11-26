package com.example.trainymvp.data

//  import androidx.room.Entity
//  import androidx.room.PrimaryKey

/**
 * Entity data class represents a single row in the database.
 */
//  @Entity(tableName = "item")
data class Item(
//    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
//    @DrawableRes
//    val icon: Int,
//    @ForeignKey(entity = TimePreset)
    val TimePreset: TimePreset
)