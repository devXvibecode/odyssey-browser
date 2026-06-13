package com.example.odysseybrowser.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.odysseybrowser.R
import com.example.odysseybrowser.ui.chat.ChatActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var editTextGoal: EditText
    private lateinit var buttonStart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        editTextGoal = findViewById(R.id.editTextGoal)
        buttonStart = findViewById(R.id.buttonStart)

        buttonStart.setOnClickListener {
            val goal = editTextGoal.text.toString().trim()
            if (goal.isNotEmpty()) {
                val intent = Intent(this, ChatActivity::class.java).apply {
                    putExtra("initial_goal", goal)
                }
                startActivity(intent)
            }
        }
    }
}