package com.example.trainymvp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseImageDao {
    @Query("SELECT * FROM exercise_image ORDER BY item_id ASC")
    fun getAllExerciseImage(): Flow<List<ExerciseImage>>

    @Query("SELECT * FROM exercise_image WHERE exercise_image_id = :id")
    fun getExerciseImage(id: Int): Flow<ExerciseImage>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(exerciseImage: ExerciseImage)

    @Update
    suspend fun update(exerciseImage: ExerciseImage)

    @Delete
    suspend fun delete(exerciseImage: ExerciseImage)

    @Transaction
    @Query("SELECT * FROM items")
    fun getWorkoutProgram(): Flow<List<WorkoutProgram>>
}