package com.example.kernel.UI

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
<<<<<<< HEAD
import androidx.appcompat.app.AppCompatActivity
import com.example.kernel.Components.InputText
import com.example.kernel.Components.SentimentApiInterface
import com.example.kernel.Components.SentimentData
import com.example.kernel.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
=======
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kernel.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
>>>>>>> cfdf3db (Project Files)

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

<<<<<<< HEAD
        submitButton.setOnClickListener {
            submitFeedback(eventId)
            finish()
        }
=======
        submitButton.setOnClickListener { submitFeedback(eventId) }
>>>>>>> cfdf3db (Project Files)
    }

    private fun submitFeedback(eventId: String?) {
        val feedback = feedbackEditText.text.toString().trim()

        if (feedback.isEmpty()) {
            feedbackEditText.error = "Please enter!"
            return
        }

<<<<<<< HEAD
        if (eventId != null) {
            fetchSentiment(feedback,eventId)
        }
    }

    private fun fetchSentiment(text: String, eventId: String) {
        val client = OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.MINUTES) // Increase connection timeout
            .readTimeout(3, TimeUnit.MINUTES) // Increase read timeout
            .writeTimeout(3, TimeUnit.MINUTES) // Increase write timeout
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://senti-api-6.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client) // Set the custom OkHttpClient
            .build()

        val api = retrofit.create(SentimentApiInterface::class.java)
        api.analyzeText(InputText(text)).enqueue(object : Callback<SentimentData> {
            override fun onResponse(call: Call<SentimentData>, response: Response<SentimentData>) {
                Log.d("Api AI/ML", "Response: $response")
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        Log.d("Sentiment", "SUCCESS: Sentiment: ${result.sentiment}, Alert: ${result.alert}")
                        val sentimentMap = mapOf(
                            "text" to text,
                            "sentiment" to result.sentiment,
                            "alert" to result.alert,
                        )

                        Firebase.firestore.collection("events")
                            .document(eventId)
                            .collection("Sentiments")
                            .add(sentimentMap)
                    } else {
                        Log.e("Sentiment", "SUCCESS but empty body")
                    }
                } else {
                    Log.e("Sentiment", "API Error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SentimentData>, t: Throwable) {
                Log.e("API Error", t.message ?: "Unknown error")
            }
        })
=======
        // Save to Firestore
        val feedbackData = hashMapOf(
            "feedback" to feedback,
            "currentUserId" to currentUserId,
            "timestamp" to com.google.firebase.firestore.FieldValue.serverTimestamp()
        )

            db.collection("events").document("$eventId").collection("feedbacks").add(feedbackData)
            .addOnSuccessListener { Toast.makeText(this, "Saved to Firestore", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { e -> Log.e("Firestore", "Error saving", e) }
>>>>>>> cfdf3db (Project Files)
    }
}