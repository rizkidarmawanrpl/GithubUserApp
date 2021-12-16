package com.erdeprof.githubuserapp

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider
import com.erdeprof.githubuserapp.databinding.ActivityMainBinding
import com.erdeprof.githubuserapp.helper.SettingViewModelFactory
import com.erdeprof.githubuserapp.ui.main.SettingPreferences
import com.erdeprof.githubuserapp.ui.main.SettingViewModel
import java.util.*
import kotlin.collections.ArrayList

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvUsers: RecyclerView
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvUsers = binding.rvUsers
        rvUsers.setHasFixedSize(true)

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        mainViewModel.itemsItem.observe(this, { itemsItem ->
            setUserData(itemsItem)
        })

        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        mainViewModel.message.observe(this, {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

        binding.inputUsername.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mainViewModel.getSearchUser(p0.toString())
            }
        })

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        settingViewModel.getThemeSettings().observe(this, {isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        })
    }

    private fun setUserData(itemsitem: List<ItemsItem>) {
        val listUser = ArrayList<User>()
        for (item in itemsitem) {
            val user = User(
                item.avatarUrl,
                item.login
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
                val i = Intent(this, SettingsActivity::class.java)
                startActivity(i)
                return true
            }
            R.id.menu_favorite -> {
                val i = Intent(this, FavoriteActivity::class.java)
                startActivity(i)
                return true
            }
            else -> return true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}