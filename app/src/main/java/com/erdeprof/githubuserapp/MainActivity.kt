package com.erdeprof.githubuserapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class MainActivity : AppCompatActivity() {
    private lateinit var rvUsers: RecyclerView
    private val list = ArrayList<User>()

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvUsers = findViewById(R.id.rv_users)
        rvUsers.setHasFixedSize(true)

        val inputUsername: TextInputEditText = findViewById(R.id.input_username)
        inputUsername.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                // Toast.makeText(this@MainActivity, p0, Toast.LENGTH_SHORT).show()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchUser(p0.toString())
            }
        })

        /*val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = findViewById<SearchView>(R.id.input_username)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                Toast.makeText(this@MainActivity, p0, Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                Toast.makeText(this@MainActivity, p0, Toast.LENGTH_SHORT).show()
                return true
            }
        })*/

        /*list.addAll(listUsers)
        showRecyclerList()*/
    }

    /*private val listUsers: ArrayList<User>
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
    }*/

    private fun searchUser(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getSearchUsers(username)
        client.enqueue(object : Callback<UserSearchResponse> {
            override fun onResponse(
                call: Call<UserSearchResponse>,
                response: Response<UserSearchResponse>
            ) {
                showLoading(false)
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserData(responseBody.items)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setUserData(itemsitem: List<ItemsItem>) {
        val dataAvatar = resources.obtainTypedArray(R.array.avatar)
        val listUser = ArrayList<User>()
        val i = 1;
        for (item in itemsitem) {
            val user = User(
                item.login,
                0,
                0,
                dataAvatar.getResourceId(i, -1),
                item.login,
                "-",
                0,
                "-"
            )
            listUser.add(user)
        }

        list.clear()
        list.addAll(listUser)
        showRecyclerList()
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

    private fun showLoading(isLoading: Boolean) {
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}