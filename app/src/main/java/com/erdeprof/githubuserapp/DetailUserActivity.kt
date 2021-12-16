package com.erdeprof.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.erdeprof.githubuserapp.database.Favorite
import com.erdeprof.githubuserapp.databinding.ActivityDetailUserBinding
import com.erdeprof.githubuserapp.ui.insert.FavoriteAddUpdateViewModel
import com.erdeprof.githubuserapp.helper.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var favoriteAddUpdateViewModel: FavoriteAddUpdateViewModel

    private var isDelete = false
    private var favorite: Favorite? = null

    private var username: String = ""
    private var avatar: String = ""
    private var follower: Int = 0
    private var following: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f

        setTitle(R.string.app_name_detail_user)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        username = user.username.toString()

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        mainViewModel.getDetailUser(username)

        mainViewModel.userDetail.observe(this, { userDetail ->
            setDetailUserData(userDetail)

            username = userDetail.login
            avatar = userDetail.avatarUrl
            follower = userDetail.followers
            following = userDetail.following
        })

        mainViewModel.message.observe(this, {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        favoriteAddUpdateViewModel = obtainViewModel(this@DetailUserActivity)

        favorite = intent.getParcelableExtra(EXTRA_FAVORITE)
        if (favorite != null) {
            isDelete = true
        } else {
            favorite = Favorite()
        }

        binding.fabFavorite.setOnClickListener { view ->
            if (view.id == R.id.fab_favorite) {
                favorite.let { favorite ->
                    favorite?.username = username
                    favorite?.avatar_url = avatar
                    favorite?.follower = follower
                    favorite?.following = following
                }
                if (isDelete) {
                    showAlertDialog(ALERT_DIALOG_DELETE)
                } else {
                    favoriteAddUpdateViewModel.insert(favorite as Favorite)
                    showToast(getString(R.string.added))
                    finish()
                }

                // Toast.makeText(this, "Fab Button", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        if (isDelete) {
            showAlertDialog(ALERT_DIALOG_CLOSE)
        } else {
            super.onBackPressed()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteAddUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteAddUpdateViewModel::class.java)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String
        if (isDialogClose) {
            dialogTitle = getString(R.string.cancel)
            dialogMessage = getString(R.string.message_cancel)
        } else {
            dialogTitle = getString(R.string.delete)
            dialogMessage = getString(R.string.message_delete)
        }
        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                if(!isDialogClose) {
                    favoriteAddUpdateViewModel.delete(favorite as Favorite)
                    showToast(getString(R.string.deleted))
                }
                finish()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun setDetailUserData(detail: UserDetailResponse) {
        val tvAvatar = binding.imgAvatar
        val tvName = binding.tvName
        val tvUsername = binding.tvUsername
        val tvRepository = binding.tvRepositoryValue
        val tvFollower = binding.tvFollowerValue
        val tvFollowing = binding.tvFollowingValue
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val favoriteItem = menu.findItem(R.id.menu_favorite)
        favoriteItem.setVisible(false)

        return super.onCreateOptionsMenu(menu)
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

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_FAVORITE = "extra_favorite"
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_follower,
            R.string.tab_text_following
        )
    }
}