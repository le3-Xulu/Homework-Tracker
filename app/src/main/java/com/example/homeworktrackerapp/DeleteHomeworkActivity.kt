package com.example.homeworktrackerapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class DeleteHomeworkActivity : AppCompatActivity() {

    private lateinit var spinnerSubjects: Spinner
    private lateinit var etDescription: EditText
    private lateinit var etDueDate: EditText
    private lateinit var btnDelete: Button

    private val db = FirebaseFirestore.getInstance()
    private val homeworkMap = mutableMapOf<String, Homework>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_homework)

        spinnerSubjects = findViewById(R.id.spinnerSubjects)
        etDescription = findViewById(R.id.etDescription)
        etDueDate = findViewById(R.id.etDueDate)
        btnDelete = findViewById(R.id.btnDeleteHomework)

        fetchHomework()

        spinnerSubjects.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val subject = parent?.getItemAtPosition(position).toString()
                val homework = homeworkMap[subject]
                homework?.let {
                    etDescription.setText(it.description)
                    etDueDate.setText(it.dueDate)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnDelete.setOnClickListener {
            val selectedSubject = spinnerSubjects.selectedItem?.toString()
            if (selectedSubject != null && homeworkMap.containsKey(selectedSubject)) {
                val homework = homeworkMap[selectedSubject]
                homework?.let {
                    db.collection("homework").document(it.id)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Homework deleted", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to delete: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }

    private fun fetchHomework() {
        db.collection("homework")
            .get()
            .addOnSuccessListener { result ->
                val subjects = mutableListOf<String>()
                for (doc in result) {
                    val hw = doc.toObject(Homework::class.java)
                    hw.id = doc.id
                    homeworkMap[hw.subject] = hw
                    subjects.add(hw.subject)
                }

                if (subjects.isNotEmpty()) {
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, subjects)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerSubjects.adapter = adapter
                } else {
                    Toast.makeText(this, "No homework to delete", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error loading homework", Toast.LENGTH_SHORT).show()
            }
    }
}
