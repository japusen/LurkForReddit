package com.example.lurkforreddit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PostEntity::class],
    version = 2
)
abstract class PostHistoryDatabase: RoomDatabase() {
    abstract val dao: PostHistoryDao
}