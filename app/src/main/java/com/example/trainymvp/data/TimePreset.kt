package com.example.trainymvp.data

// import androidx.room.Entity
// import androidx.room.PrimaryKey

/**
 * Entity data class represents a single row in the database.
 */
// @Entity(tableName = "timePreset")
class TimePreset (
    // @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val exerciseTimeInSeconds: Int = 45,
    val restTimeInSeconds: Int = 25
)