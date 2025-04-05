package com.example.kernel.UI

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.kernel.Adapters.OrganizerPagerAdapter
import com.example.kernel.Adapters.SignInUpPagerAdapter
import com.example.kernel.R

class OrganizerActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizer)

        viewPager = findViewById(R.id.viewPager)

        val adapter = OrganizerPagerAdapter(this)
        viewPager.adapter = adapter
    }
}