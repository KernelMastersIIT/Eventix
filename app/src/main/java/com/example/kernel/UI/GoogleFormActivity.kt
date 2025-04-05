package com.example.kernel.UI

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kernel.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GoogleFormActivity : AppCompatActivity() {

    private lateinit var tvEventName: TextView
    private lateinit var tvEventDate: TextView
    private lateinit var tvEventTime: TextView
    private lateinit var feedbackEditText: EditText
    private lateinit var submitButton: Button

    private val db = FirebaseFirestore.getInstance()
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: "Anonymous"
    private val googleFormUrl = "https://docs.google.com/forms/d/e/1ta9lvGYcnyANctpgVoYUZV0UjuTZIdcTs3hsnJ1rpiY/formResponse"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_form)

        val eventName = intent.getStringExtra("eventName")
        val eventDate = intent.getStringExtra("eventDate")
        val eventTime = intent.getStringExtra("eventTime")
        val eventId = intent.getStringExtra("eventId")

        tvEventName = findViewById(R.id.tvEventTitle)
        tvEventDate = findViewById(R.id.tvEventDate)
        tvEventTime = findViewById(R.id.tvEventTime)
        feedbackEditText = findViewById(R.id.feedbackEditText)
        submitButton = findViewById(R.id.submitButton)

        tvEventName.text = eventName
        tvEventDate.text = eventDate
        tvEventTime.text = eventTime

        submitButton.setOnClickListener { submitFeedback(eventId) }
    }

    private fun submitFeedback(eventId: String?) {
        val feedback = feedbackEditText.text.toString().trim()

        if (feedback.isEmpty()) {
            feedbackEditText.error = "Please enter!"
            return
        }

        // Save to Firestore
        val feedbackData = hashMapOf(
            "feedback" to feedback,
            "currentUserId" to currentUserId,
            "timestamp" to com.google.firebase.firestore.FieldValue.serverTimestamp()
        )

            db.collection("events").document("$eventId").collection("feedbacks").add(feedbackData)
            .addOnSuccessListener { Toast.makeText(this, "Saved to Firestore", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { e -> Log.e("Firestore", "Error saving", e) }
    }
}