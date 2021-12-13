package com.erdeprof.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.erdeprof.githubuserapp.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding

    companion object {
        const val EXTRA_USER = "extra_user"
        private const val TAG = "DetailUserActivity"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_follower,
            R.string.tab_text_following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f

        setTitle("Detail User")

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        mainViewModel.userDetail.observe(this, { userDetail ->
            setDetailUserData(userDetail)
        })

        mainViewModel.message.observe(this, {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        val username = user.username.toString()

        mainViewModel.getDetailUser(username)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun getDetailUser(username: String) {
        val client = ApiConfig.getApiService().getDetailUsers(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        setDetailUserData(responseBody)
                    }
                } else {
                    Toast.makeText(this@DetailUserActivity, "Data gagal dimuat!", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onFailure1: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                Toast.makeText(this@DetailUserActivity, "Data gagal dimuat!", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure2: ${t.message}")
            }
        })
    }

    private fun setDetailUserData(detail: UserDetailResponse) {
        val tvAvatar: ImageView = findViewById(R.id.img_avatar)
        val tvName: TextView = findViewById(R.id.tv_name)
        val tvUsername: TextView = findViewById(R.id.tv_username)
        val tvRepository: TextView = findViewById(R.id.tv_repository_value)
        val tvFollower: TextView = findViewById(R.id.tv_follower_value)
        val tvFollowing: TextView = findViewById(R.id.tv_following_value)
        val tvLocation: TextView = findViewById(R.id.tv_location_value)
        val tvCompany: TextView = findViewById(R.id.tv_company_value)

        Glide.with(this@DetailUserActivity)
            .load(detail.avatarUrl)
            .into(tvAvatar)
        tvName.text = detail.name
        tvUsername.text = "@" + detail.login
        tvRepository.text = detail.publicRepos.toString()
        tvFollower.text = detail.followers.toString()
        tvFollowing.text = detail.following.toString()
        tvLocation.text = detail.location
        tvCompany.text = detail.company
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