package com.erdeprof.githubuserapp.ui.insert

import android.app.Application
import androidx.lifecycle.ViewModel
import com.erdeprof.githubuserapp.database.Favorite
import com.erdeprof.githubuserapp.repository.FavoriteRepository

class FavoriteAddUpdateViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun insert(favorite: Favorite) {
        mFavoriteRepository.insert(favorite)
    }

    fun delete(favorite: Favorite) {
        mFavoriteRepository.delete(favorite)
    }
}