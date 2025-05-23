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

    @Query("SELECT * FROM exercise_image WHERE item_id = :id")
    fun getExerciseImageByItemId(id: Int) : Flow<List<ExerciseImage>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(exerciseImage: ExerciseImage)

    @Update
    suspend fun update(exerciseImage: ExerciseImage)

    @Delete
    suspend fun delete(exerciseImage: ExerciseImage)

    @Query("DELETE FROM exercise_image WHERE item_id = :id")
    suspend fun resetTable(id: Int)

    @Transaction
    @Query("SELECT * FROM items")
    fun getWorkoutProgram(): Flow<List<WorkoutProgram>>
}