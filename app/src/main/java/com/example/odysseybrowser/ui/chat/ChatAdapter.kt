package com.example.odysseybrowser.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.odysseybrowser.R
import com.example.odysseybrowser.model.ChatMessage

class ChatAdapter(
    private var messages: List<ChatMessage>
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textViewMessage)

        fun bind(message: ChatMessage) {
            textView.text = message.message
            // Set background based on whether it's a user or bot message
            val background = if (message.isUser) {
                R.drawable.bg_user_message
            } else {
                R.drawable.bg_bot_message
            }
            textView.setBackgroundResource(background)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size

    fun submitList(newList: List<ChatMessage>) {
        messages = newList
        notifyDataSetChanged()
    }
}