package com.erdeprof.githubuserapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.erdeprof.githubuserapp.ui.main.FollowerFragment

class SectionsPagerAdapter(activity: AppCompatActivity, private  val username: String) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return FollowerFragment.newInstance(position, username)
    }

}