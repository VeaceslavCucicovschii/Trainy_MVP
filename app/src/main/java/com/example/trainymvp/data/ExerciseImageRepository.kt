package com.example.trainymvp.data

import kotlinx.coroutines.flow.Flow

interface ExerciseImageRepository {
    fun getAllIExerciseImageStream(): Flow<List<ExerciseImage>>
    fun getExerciseImageStream(id: Int): Flow<ExerciseImage?>
    suspend fun insertExerciseImage(exerciseImage: ExerciseImage)
    suspend fun deleteExerciseImage(exerciseImage: ExerciseImage)
    suspend fun updateExerciseImage(exerciseImage: ExerciseImage)
    fun getWorkoutProgram(): Flow<List<WorkoutProgram>>
}