package com.erdeprof.githubuserapp.database

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val avatar: String?,
    val username: String?
) : Parcelable
