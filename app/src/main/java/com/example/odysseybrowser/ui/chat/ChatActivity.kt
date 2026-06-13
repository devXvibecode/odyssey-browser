package com.example.odysseybrowser.ui.chat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.odysseybrowser.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var adapter: ChatAdapter
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSend: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        editTextMessage = findViewById(R.id.editTextMessage)
        buttonSend = findViewById(R.id.buttonSend)

        // Set up RecyclerView
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerViewChat)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ChatAdapter(emptyList())
        recyclerView.adapter = adapter

        // Observe messages from ViewModel
        lifecycleScope.launch {
            viewModel.messages.collectLatest { messages ->
                adapter.submitList(messages)
                // Scroll to the bottom
                recyclerView.scrollToPosition(messages.size - 1)
            }
        }

        // Set up send button
        buttonSend.setOnClickListener {
            val message = editTextMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(message)
                editTextMessage.text.clear()
            }
        }

        // Handle initial goal from intent
        val initialGoal = intent.getStringExtra("initial_goal")
        if (initialGoal?.isNotEmpty() == true) {
            viewModel.sendMessage(initialGoal)
        }
    }
}