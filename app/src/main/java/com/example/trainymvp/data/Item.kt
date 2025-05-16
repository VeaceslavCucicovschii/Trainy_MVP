package com.example.trainymvp.data

import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Icon
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "items", indices = [Index(value = ["title"], unique = true)])
data class Item(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    val itemId: Int = 0,
    // val icon: Icon,
    val title: String,
    val description: String,
)