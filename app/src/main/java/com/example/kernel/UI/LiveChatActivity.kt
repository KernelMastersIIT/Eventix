package com.example.kernel.UI

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat



import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.Timestamp
import com.example.kernel.R
import com.example.kernel.UI.Fragments.ChatAdapter
import com.example.kernel.Components.ChatMessage
import com.example.kernel.Components.InputText
import com.example.kernel.Components.SentimentApiInterface
import com.example.kernel.Components.SentimentData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LiveChatActivity : AppCompatActivity() {
    private lateinit var adapter: ChatAdapter
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton

    private lateinit var eventId: String

    private val CHANNEL_ID = "event_alert_channel"
    private val NOTIFICATION_ID = 1001


    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: "Anonymous"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_chat)

        eventId = intent.getStringExtra("eventId") ?: return

        chatRecyclerView = findViewById(R.id.recyclerViewChat)
        messageEditText = findViewById(R.id.editTextMessage)
        sendButton = findViewById(R.id.buttonSend)

        adapter = ChatAdapter(currentUserId)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = adapter

        sendButton.setOnClickListener {
            val text = messageEditText.text.toString().trim()
            if (text.isNotEmpty()) {
                sendMessage(text)
                messageEditText.setText("")
            }
        }

        listenForMessages()
    }


    private fun sendMessage(text: String) {
        val message = ChatMessage(currentUserId, text,Timestamp.now())
        FirebaseFirestore.getInstance().collection("events")
            .document(eventId)
            .collection("chats")
            .add(message)
        fetchsentiment(text)

    }


    private fun listenForMessages() {
        Firebase.firestore.collection("events")
            .document(eventId)
            .collection("chats")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, _ ->
                val messages = snapshot?.toObjects(ChatMessage::class.java)
                messages?.let {
                    adapter.submitList(it)
                    chatRecyclerView.scrollToPosition(it.size - 1)
                }
            }
    }

    private fun fetchsentiment(text: String) {
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // Increase connection timeout
            .readTimeout(60, TimeUnit.SECONDS) // Increase read timeout
            .writeTimeout(60, TimeUnit.SECONDS) // Increase write timeout
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
                        if (result.alert == "alert") {
                            showAlertNotification(text)
                        }
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
    }
    private fun showAlertNotification(message: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Event Alerts"
            val descriptionText = "Notifications for flagged messages in events"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Check permission (required on Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
                return
            }
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // use a valid icon here
            .setContentTitle("⚠ Event Alert")
            .setContentText("Potential harmful message: \"$message\"")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}