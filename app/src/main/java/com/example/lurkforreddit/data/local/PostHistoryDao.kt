package com.example.lurkforreddit.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PostHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Query("SELECT * from postentity ORDER BY id DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Query("DELETE FROM postentity")
    suspend fun clearPostHistory()
}