package com.example.odysseybrowser.ui.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.odysseybrowser.data.local.ChatDatabase
import com.example.odysseybrowser.data.local.ChatEntity
import com.example.odysseybrowser.data.remote.ChatRequest
import com.example.odysseybrowser.data.remote.RetrofitClient
import com.example.odysseybrowser.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val chatDatabase = ChatDatabase.getDatabase(application)

    init {
        loadMessages()
    }

    private suspend fun loadMessages() {
        val entities = chatDatabase.chatDao().getAllMessages()
        val chatMessages = entities.map { ChatMessage(it.message, it.isUser, it.timestamp) }
        _messages.value = chatMessages
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            // 1. Add the user message to the database and update the UI
            val userMessage = ChatMessage(message, true)
            val userEntity = ChatEntity(message = message, isUser = true, timestamp = System.currentTimeMillis())
            chatDatabase.chatDao().insertAll(userEntity)
            updateMessages { it + userMessage }

            // 2. Call the API to get the bot response
            val request = ChatRequest(message)
            val response: Response<ChatResponse> = RetrofitClient.apiService.sendMessage(request).execute()
            if (response.isSuccessful) {
                val botResponse = response.body()?.reply ?: "Sorry, I couldn't understand that."
                val botMessage = ChatMessage(botResponse, false)
                val botEntity = ChatEntity(message = botResponse, isUser = false, timestamp = System.currentTimeMillis())
                chatDatabase.chatDao().insertAll(botEntity)
                updateMessages { it + botMessage }
            } else {
                val errorMessage = "Error: ${response.code()}"
                val errorEntity = ChatEntity(message = errorMessage, isUser = false, timestamp = System.currentTimeMillis())
                chatDatabase.chatDao().insertAll(errorEntity)
                updateMessages { it + ChatMessage(errorMessage, false) }
            }
        }
    }

    private fun updateMessages(block: (List<ChatMessage>) -> List<ChatMessage>) {
        viewModelScope.launch {
            val current = _messages.value
            _messages.value = block(current)
        }
    }
}