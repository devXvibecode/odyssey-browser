package com.example.odysseybrowser.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    suspend fun getAllMessages(): List<ChatEntity>

    @Insert
    suspend fun insertAll(vararg entities: ChatEntity)
}