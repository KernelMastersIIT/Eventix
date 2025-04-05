package com.example.kernel.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kernel.UI.Fragments.LoginFragment
import com.example.kernel.UI.Fragments.SignUpFragment

class SignInUpPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoginFragment()
            1 -> SignUpFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}