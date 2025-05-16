package com.example.homeworktrackerapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class UpdateHomeworkActivity : AppCompatActivity() {

    private lateinit var spinnerSubjects: Spinner
    private lateinit var etDescription: EditText
    private lateinit var etDueDate: EditText
    private lateinit var btnUpdate: Button

    private val db = FirebaseFirestore.getInstance()

    // Map subject -> Homework (includes Firestore document ID)
    private val homeworkMap = mutableMapOf<String, Homework>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_homework)

        // View references
        spinnerSubjects = findViewById(R.id.spinnerSubjects)
        etDescription = findViewById(R.id.etUpdateDescription)
        etDueDate = findViewById(R.id.etUpdateDueDate)
        btnUpdate = findViewById(R.id.btnUpdateHomework)

        fetchHomeworkFromFirestore()

        spinnerSubjects.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val subject = parent?.getItemAtPosition(position).toString()
                homeworkMap[subject]?.let { hw ->
                    etDescription.setText(hw.description)
                    etDueDate.setText(hw.dueDate)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnUpdate.setOnClickListener {
            val subject = spinnerSubjects.selectedItem.toString()
            val newDescription = etDescription.text.toString().trim()
            val newDueDate = etDueDate.text.toString().trim()

            if (subject.isEmpty() || newDescription.isEmpty() || newDueDate.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val homework = homeworkMap[subject]
            if (homework != null) {
                val updatedData = mapOf(
                    "description" to newDescription,
                    "dueDate" to newDueDate
                )

                db.collection("homework").document(homework.id)
                    .update(updatedData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Homework updated!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Update failed: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun fetchHomeworkFromFirestore() {
        db.collection("homework")
            .get()
            .addOnSuccessListener { result ->
                val subjects = mutableListOf<String>()
                for (doc in result) {
                    val hw = doc.toObject(Homework::class.java)
                    hw.id = doc.id  // Save document ID
                    homeworkMap[hw.subject] = hw
                    subjects.add(hw.subject)
                }

                if (subjects.isNotEmpty()) {
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, subjects)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerSubjects.adapter = adapter
                } else {
                    Toast.makeText(this, "No homework found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch homework", Toast.LENGTH_SHORT).show()
            }
    }
}
