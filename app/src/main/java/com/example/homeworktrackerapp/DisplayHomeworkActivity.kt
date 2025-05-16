package com.example.homeworktrackerapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class DisplayHomeworkActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeworkAdapter
    private val homeworkList = mutableListOf<Homework>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_homework)

        recyclerView = findViewById(R.id.recyclerViewHomework)
        adapter = HomeworkAdapter(homeworkList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fetchHomeworks()
    }

    private fun fetchHomeworks() {
        db.collection("homework")
            .get()
            .addOnSuccessListener { result ->
                homeworkList.clear()
                for (doc in result) {
                    val item = doc.toObject(Homework::class.java)
                    homeworkList.add(item)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
