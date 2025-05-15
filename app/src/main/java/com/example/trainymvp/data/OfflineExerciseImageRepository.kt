package com.example.trainymvp.data

import kotlinx.coroutines.flow.Flow

class OfflineExerciseImageRepository(private val ExerciseImageDao: ExerciseImageDao) : ExerciseImageRepository {
    override fun getAllIExerciseImageStream(): Flow<List<ExerciseImage>> = ExerciseImageDao.getAllExerciseImage()
    override fun getExerciseImageStream(id: Int): Flow<ExerciseImage?> = ExerciseImageDao.getExerciseImage(id)
    override fun getExerciseImageByItemId(id: Int): Flow<List<ExerciseImage>> = ExerciseImageDao.getExerciseImageByItemId(id)
    override suspend fun insertExerciseImage(exerciseImage: ExerciseImage) = ExerciseImageDao.insert(exerciseImage)
    override suspend fun deleteExerciseImage(exerciseImage: ExerciseImage) = ExerciseImageDao.delete(exerciseImage)
    override suspend fun updateExerciseImage(exerciseImage: ExerciseImage) = ExerciseImageDao.update(exerciseImage)
    override fun getWorkoutProgram(): Flow<List<WorkoutProgram>> = ExerciseImageDao.getWorkoutProgram()
}