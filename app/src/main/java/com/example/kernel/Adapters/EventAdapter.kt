package com.example.kernel.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kernel.Components.EventData
import com.example.kernel.R
import com.example.kernel.UI.GoogleFormActivity
import com.example.kernel.UI.LiveChatActivity

class EventAdapter(val context: Context, private var list: ArrayList<EventData>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>(){

    private lateinit var myListener: EventAdapter.onItemClickListener

    interface onItemClickListener {
        fun onItemClicking(position: Int)
    }

    fun setOnItemClickListener(listener : onItemClickListener){
        myListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(position: Int, product: EventData){
        list.add(position,product)
        notifyDataSetChanged()
    }

    fun updateData(newList: ArrayList<EventData>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.each_event_item, parent, false)
        return EventViewHolder(itemView,myListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentItem = list[position]

        holder.eventTitle.text = currentItem.eventTitle
        holder.eventDetail.text = currentItem.description
        //holder.source.text="Source: ${currentItem.publisher.name} "
        holder.eventHashtags.text=currentItem.hashtags
        holder.eventDate.text=currentItem.date
        holder.eventTime.text=currentItem.time
        holder.eventVenue.text=currentItem.venue

        holder.liveChatButton.setOnClickListener {
            val intent = Intent(context, LiveChatActivity::class.java)
            intent.putExtra("eventId", currentItem.eventId)
            context.startActivity(intent)
        }

        holder.feedbackButton.setOnClickListener {
            val intent = Intent(context, GoogleFormActivity::class.java)
            intent.putExtra("eventName", currentItem.eventTitle)
            intent.putExtra("eventDate", currentItem.date)
            intent.putExtra("eventTime", currentItem.time)
            intent.putExtra("eventId", currentItem.eventId)
            context.startActivity(intent)
        }

        /*Glide.with(holder.itemView.context)
            .load(currentItem.thumbnail)
            .placeholder(R.drawable.baseline_downloading)
            .error(R.drawable.baseline_error_outline)
            .into(holder.image)*/
    }

    class EventViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        var eventTitle: TextView = itemView.findViewById(R.id.tv_event_name)
        var eventDetail: TextView = itemView.findViewById(R.id.tv_event_detail)
        var eventHashtags: TextView = itemView.findViewById(R.id.tv_event_hashtags)
        var eventDate: TextView = itemView.findViewById(R.id.tv_event_date)
        var eventTime: TextView = itemView.findViewById(R.id.tv_event_time)
        var eventVenue: TextView = itemView.findViewById(R.id.tv_event_venue)
        val liveChatButton: View = itemView.findViewById(R.id.live_chat_button)
        val feedbackButton: Button = itemView.findViewById(R.id.btn_feedback)


        init {
            //shareButton= itemView.findViewById(R.id.shareButton)
            //downloadButton=itemView.findViewById((R.id.download))
            itemView.setOnClickListener {
                listener.onItemClicking(adapterPosition)
            }


        }


    }

    /*private fun shareArticle(context: Context, title: String, url: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Check out this news article!")
            putExtra(Intent.EXTRA_TEXT, "$title\nRead more: $url")
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }*/


}