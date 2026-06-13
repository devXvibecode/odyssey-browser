package com.example.odysseybrowser.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val message: String,
    val isUser: Boolean,
    val timestamp: Long
)