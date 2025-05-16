package com.example.homeworktrackerapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AddHomeworkActivity : AppCompatActivity() {

    private lateinit var etSubject: EditText
    private lateinit var etDescription: EditText
    private lateinit var etDueDate: EditText
    private lateinit var btnSave: Button
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_homework)

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance()

        // Initialize views
        etSubject = findViewById(R.id.etSubject)
        etDescription = findViewById(R.id.etDescription)
        etDueDate = findViewById(R.id.etDueDate)
        btnSave = findViewById(R.id.btnSave)

        // Set Date Picker
        etDueDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,
                { _, year, month, day ->
                    val formattedDate = "%04d-%02d-%02d".format(year, month + 1, day)
                    etDueDate.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Save button action
        btnSave.setOnClickListener {
            val subject = etSubject.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val dueDate = etDueDate.text.toString().trim()

            if (subject.isEmpty() || description.isEmpty() || dueDate.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val homework = Homework(subject, description, dueDate)

            db.collection("homework")
                .add(homework)
                .addOnSuccessListener {
                    Toast.makeText(this, "Homework saved!", Toast.LENGTH_SHORT).show()
                    finish() // close activity
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
