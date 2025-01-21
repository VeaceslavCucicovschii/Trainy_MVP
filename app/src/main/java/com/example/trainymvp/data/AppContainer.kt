package com.example.trainymvp.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val itemsRepository: ItemsRepository
    val exerciseImageRepository: ExerciseImageRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(TrainyDatabase.getDatabase(context).itemDao())
    }

    /**
     * Implementation for [ExerciseImageRepository]
     */
    override val exerciseImageRepository: ExerciseImageRepository by lazy {
        OfflineExerciseImageRepository(TrainyDatabase.getDatabase(context).exerciseImageDao())
    }
}