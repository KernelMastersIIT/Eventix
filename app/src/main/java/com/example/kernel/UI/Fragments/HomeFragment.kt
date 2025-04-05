package com.example.kernel.UI.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
<<<<<<< HEAD
=======
import android.widget.Toast
>>>>>>> cfdf3db (Project Files)
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kernel.Adapters.EventAdapter
<<<<<<< HEAD
import com.example.kernel.Components.EventData
import com.example.kernel.R
import com.example.kernel.UI.LiveChatActivity
import com.example.kernel.UI.ProfileActivity
=======
import com.example.kernel.Components.ChatMessage
import com.example.kernel.Components.EventData
import com.example.kernel.R
import com.example.kernel.UI.LiveChatActivity
import com.example.kernel.UI.pfp
>>>>>>> cfdf3db (Project Files)
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
<<<<<<< HEAD
import com.google.firebase.database.FirebaseDatabase
=======
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
>>>>>>> cfdf3db (Project Files)


class HomeFragment :  Fragment(R.layout.fragment_home)  {

    private lateinit var recyclerView: RecyclerView
    private var eventList = ArrayList<EventData>()
    private lateinit var eventAdapter: EventAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var progressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.progressBar)
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        recyclerView = view.findViewById(R.id.recyclerView)
        val overlappingIcon = view.findViewById<ImageView>(R.id.overlapping_icon)
        //swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        val database = FirebaseDatabase.getInstance().getReference("Events")
        progressBar.visibility = View.VISIBLE

        database.get().addOnSuccessListener { snack ->
            eventList.clear()
            for(key in snack.children){
                Log.d("EventData", "${key.value}")
                val data = key.getValue(EventData::class.java)
                val eventId = key.key
                if (data != null && eventId != null) {
                    data.eventId = eventId
                    eventList.add(data)
                }
            }

<<<<<<< HEAD
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            eventAdapter = EventAdapter(requireContext(), eventList)
            recyclerView.adapter = eventAdapter
=======
            context?.let { safeContext ->
                recyclerView.layoutManager = LinearLayoutManager(safeContext)
                eventAdapter = EventAdapter(safeContext, eventList)
                recyclerView.adapter = eventAdapter

                eventAdapter.setOnItemClickListener(object : EventAdapter.onItemClickListener {
                    override fun onItemClicking(position: Int) {
                        val intent = Intent(safeContext, LiveChatActivity::class.java)
                        startActivity(intent)
                    }
                })
            }
>>>>>>> cfdf3db (Project Files)


            auth.currentUser?.let { user ->
                overlappingIcon?.let {
                    loadProfilePicture(user.uid, it)
                }
            }


            fab.setOnClickListener {
<<<<<<< HEAD
                startActivity(Intent(requireContext(), ProfileActivity::class.java))
=======
                startActivity(Intent(requireContext(), pfp::class.java))
>>>>>>> cfdf3db (Project Files)
            }

            progressBar.visibility = View.GONE

<<<<<<< HEAD
            eventAdapter.setOnItemClickListener(object : EventAdapter.onItemClickListener {
                override fun onItemClicking(position: Int) {
                    val intent = Intent(requireContext(), LiveChatActivity::class.java)
                    startActivity(intent)
                }
            })
=======

>>>>>>> cfdf3db (Project Files)
        }
    }

    override fun onResume() {
        super.onResume()
        val overlappingIcon = view?.findViewById<ImageView>(R.id.overlapping_icon)
        auth.currentUser?.let { user ->
            overlappingIcon?.let {
                loadProfilePicture(user.uid, it)
            }
        }

    }

    private fun loadProfilePicture(userId: String, profileImageView: ImageView) {
        val sharedPreferences = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val profilePicUri = sharedPreferences.getString("${userId}_profilePicUri", null)

        if (profilePicUri != null) {
            Glide.with(this)
                .load(Uri.parse(profilePicUri))
                .placeholder(R.drawable.baseline_person_24)
                .circleCrop()
                .into(profileImageView)
        }
    }
}