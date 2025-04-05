package com.example.kernel.UI.Fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kernel.Components.ChatMessage
import com.example.kernel.R

class ChatAdapter(private val currentUserId: String) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private val messages = mutableListOf<ChatMessage>()

    fun submitList(newMessages: List<ChatMessage>?) {
        messages.clear()
        if (newMessages != null) {
            messages.addAll(newMessages)
        }
        notifyDataSetChanged()
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.messageText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val msg = messages[position]
        holder.messageText.text = msg.message
        val layoutParams = holder.messageText.layoutParams as FrameLayout.LayoutParams

        // Optionally color different user's messages
        if (msg.senderId == currentUserId) {
            holder.messageText.setBackgroundResource(R.drawable.bg_message_me)
            layoutParams.gravity = android.view.Gravity.END
            holder.messageText.setTextColor(android.graphics.Color.WHITE)
        } else {
            holder.messageText.setBackgroundResource(R.drawable.bg_message_other)
            layoutParams.gravity = android.view.Gravity.START
            holder.messageText.setTextColor(android.graphics.Color.BLACK)
        }

        holder.messageText.layoutParams = layoutParams
    }

}