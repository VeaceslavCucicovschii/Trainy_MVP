package com.example.trainymvp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [Item::class, ExerciseImage::class], version = 1, exportSchema = false)
abstract class TrainyDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun exerciseImageDao(): ExerciseImageDao

    companion object {
        @Volatile
        private var Instance: TrainyDatabase? = null

        fun getDatabase(context: Context): TrainyDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TrainyDatabase::class.java, "item_database")
                    /**
                     * Setting this option in your app's database builder means that Room
                     * permanently deletes all data from the tables in your database when it
                     * attempts to perform a migration with no defined migration path.
                     */
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}