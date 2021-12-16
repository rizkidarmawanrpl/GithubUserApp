package com.erdeprof.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.erdeprof.githubuserapp.databinding.ActivityFavoriteBinding
import com.erdeprof.githubuserapp.ui.main.FavoriteAdapter
import com.erdeprof.githubuserapp.ui.main.FavoriteViewModel
import com.erdeprof.githubuserapp.helper.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private var _activityFavoriteBinding: ActivityFavoriteBinding? = null
    private val binding get() = _activityFavoriteBinding

    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setTitle(R.string.app_name_favorite)

        val favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllFavorites().observe(this, { favoriteList ->
            if (favoriteList != null) {
                adapter.setListFavorites(favoriteList)
            }
        })

        adapter = FavoriteAdapter()

        binding?.rvFavorites?.layoutManager = LinearLayoutManager(this)
        binding?.rvFavorites?.setHasFixedSize(true)
        binding?.rvFavorites?.adapter = adapter
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavoriteBinding = null
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val favoriteItem = menu.findItem(R.id.menu_favorite)
        favoriteItem.setVisible(false)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
                val i = Intent(this, SettingsActivity::class.java)
                startActivity(i)
                return true
            }
            else -> return true
        }
    }
}