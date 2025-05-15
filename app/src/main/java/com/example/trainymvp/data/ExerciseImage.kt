package com.example.trainymvp.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "exercise_image",
    foreignKeys = [
        ForeignKey(
            entity = Item::class,
            parentColumns = arrayOf("item_id"),
            childColumns = arrayOf("item_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExerciseImage(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exercise_image_id")
    val exerciseImageId: Int = 0,
    val imageData: ByteArray,
    val order: Int,
    @ColumnInfo(name = "item_id")
    val itemId: Int
)

data class WorkoutProgram(
    @Embedded val item: Item,
    @Relation(
        parentColumn = "item_id",
        entityColumn = "item_id"
    )
    val workoutProgram: List<ExerciseImage>
)