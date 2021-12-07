package com.erdeprof.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var rvUsers: RecyclerView
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvUsers = findViewById(R.id.rv_users)
        rvUsers.setHasFixedSize(true)

        list.addAll(listUsers)
        showRecyclerList()
    }

    private val listUsers: ArrayList<User>
    get() {
        val dataName = resources.getStringArray(R.array.name)
        val dataFollower = resources.getStringArray(R.array.followers)
        val dataFollowing = resources.getStringArray(R.array.following)
        val dataAvatar = resources.obtainTypedArray(R.array.avatar)
        val dataUsername = resources.getStringArray(R.array.username)
        val dataLocation = resources.getStringArray(R.array.location)
        val dataRepository = resources.getStringArray(R.array.repository)
        val dataCompany = resources.getStringArray(R.array.company)
        val listUser = ArrayList<User>()
        for (i in dataName.indices) {
            val user = User(
                dataName[i],
                dataFollower[i].toInt(),
                dataFollowing[i].toInt(),
                dataAvatar.getResourceId(i, -1),
                dataUsername[i],
                dataLocation[i],
                dataRepository[i].toInt(),
                dataCompany[i]
            )
            listUser.add(user)
        }

        return listUser;
    }

    private fun showRecyclerList() {
        rvUsers.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(list)
        rvUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User) {
        val activityDetailUser = Intent(this@MainActivity, DetailUserActivity::class.java)
        activityDetailUser.putExtra(DetailUserActivity.EXTRA_USER, user)
        startActivity(activityDetailUser)
    }
}