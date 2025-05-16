package com.example.homeworktrackerapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up navigation to AddHomeworkActivity
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        btnAdd.setOnClickListener {
            val intent = Intent(this, AddHomeworkActivity::class.java)
            startActivity(intent)
        }

        // Set up navigation to View....
        val btnView = findViewById<Button>(R.id.btnRead)
        btnView.setOnClickListener {
            val intent = Intent(this, DisplayHomeworkActivity::class.java)
            startActivity(intent)
        }

        // Set up navigation to update....
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        btnUpdate.setOnClickListener {
            val intent = Intent(this, UpdateHomeworkActivity::class.java)
            startActivity(intent)
        }

        // Set up navigation to update....
        val btnDel = findViewById<Button>(R.id.btnDelete)
        btnDel.setOnClickListener {
            val intent = Intent(this, DeleteHomeworkActivity::class.java)
            startActivity(intent)
        }

    }
}
