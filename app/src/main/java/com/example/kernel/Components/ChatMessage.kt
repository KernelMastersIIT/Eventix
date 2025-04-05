package com.example.kernel.Components


import com.google.firebase.Timestamp
data class ChatMessage(val senderId: String = "",
                       val message: String = "",
                       val timestamp: Timestamp  = Timestamp.now()
)
