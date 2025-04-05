package com.example.kernel.UI.Fragments.Organizer

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
<<<<<<< HEAD
import android.content.Intent
=======
>>>>>>> cfdf3db (Project Files)
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.example.kernel.Components.EventData
import com.example.kernel.R
<<<<<<< HEAD
import com.example.kernel.UI.SignInUp
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
=======
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
>>>>>>> cfdf3db (Project Files)
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class EventFragment : Fragment(R.layout.fragment_event) {

<<<<<<< HEAD
    private lateinit var auth: FirebaseAuth
    private lateinit var btnAddEvent : Button
    private lateinit var btnLogOut : Button
=======
    private lateinit var btnAddEvent : FloatingActionButton
>>>>>>> cfdf3db (Project Files)
    private lateinit var database: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

<<<<<<< HEAD
        auth = FirebaseAuth.getInstance()
        btnAddEvent = view.findViewById(R.id.btnAddEvent)
        btnLogOut = view.findViewById(R.id.btnLogOut)
=======
        btnAddEvent = view.findViewById(R.id.btnAddEvent)
>>>>>>> cfdf3db (Project Files)
        database = FirebaseDatabase.getInstance().getReference("Events")

        btnAddEvent.setOnClickListener {
            showEventDialog(requireContext())
        }
<<<<<<< HEAD

        btnLogOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(this.context, SignInUp::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
=======
>>>>>>> cfdf3db (Project Files)
    }

    private fun showEventDialog(context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_event, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Create Event")

        val etEventTitle = dialogView.findViewById<EditText>(R.id.et_event_title)
        val etHashtags = dialogView.findViewById<EditText>(R.id.et_hashtags)
        val etDescription = dialogView.findViewById<EditText>(R.id.et_description)
        val etVenue = dialogView.findViewById<EditText>(R.id.et_venue)
        val btnPickDate = dialogView.findViewById<Button>(R.id.btn_pick_date)
        val tvSelectedDate = dialogView.findViewById<TextView>(R.id.tv_selected_date)
        val btnPickTime = dialogView.findViewById<Button>(R.id.btn_pick_time)
        val tvSelectedTime = dialogView.findViewById<TextView>(R.id.tv_selected_time)
        val etPostLink = dialogView.findViewById<EditText>(R.id.et_post_link)

        val calendar = Calendar.getInstance()

        // Date Picker
        btnPickDate.setOnClickListener {
            val datePicker = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    tvSelectedDate.text = "Date: $selectedDate"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Time Picker
        btnPickTime.setOnClickListener {
            val timePicker = TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    tvSelectedTime.text = "Time: $selectedTime"
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePicker.show()
        }

        builder.setPositiveButton("Add Event") { _, _ ->
            val eventTitle = etEventTitle.text.toString()
            val hashtags = etHashtags.text.toString()
            val description = etDescription.text.toString()
            val venue = etVenue.text.toString()
            val date = tvSelectedDate.text.toString().replace("Date: ", "")
            val time = tvSelectedTime.text.toString().replace("Time: ", "")
            val postLink = etPostLink.text.toString()

            val eventData = EventData(eventTitle, hashtags, description, venue, date, time, postLink)

            val key =database.push().key
            if (key != null) {
                database.child(key).setValue(eventData)
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

        builder.create().show()
    }
}