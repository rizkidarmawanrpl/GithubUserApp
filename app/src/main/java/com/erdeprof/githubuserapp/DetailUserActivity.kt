package com.erdeprof.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_follower,
            R.string.tab_text_following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        setTitle("Detail User")

        val tvAvatar: ImageView = findViewById(R.id.img_avatar)
        val tvName: TextView = findViewById(R.id.tv_name)
        val tvUsername: TextView = findViewById(R.id.tv_username)
        val tvRepository: TextView = findViewById(R.id.tv_repository_value)
        val tvFollower: TextView = findViewById(R.id.tv_follower_value)
        val tvFollowing: TextView = findViewById(R.id.tv_following_value)
        val tvLocation: TextView = findViewById(R.id.tv_location_value)
        val tvCompany: TextView = findViewById(R.id.tv_company_value)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        tvAvatar.setImageResource(user.avatar)
        tvName.text = user.name
        tvUsername.text = "@" + user.username
        tvRepository.text = user.repository.toString()
        tvFollower.text = user.follower.toString()
        tvFollowing.text = user.following.toString()
        tvLocation.text = user.location
        tvCompany.text = user.company

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
                val i = Intent(this, MenuActivity::class.java)
                startActivity(i)
                return true
            }
            else -> return true
        }
    }
}