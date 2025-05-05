package com.example.trainymvp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM items ORDER BY title ASC")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE item_id = :id")
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * FROM items WHERE title = :title")
    fun getItemByTitle(title: String): Flow<Item>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)
}