package com.example.trainymvp.data

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
//    @DrawableRes
//    val icon: Int,
//    @ForeignKey(entity = TimePreset)
    val TimePreset: TimePreset
)