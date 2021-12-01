package com.erdeprof.githubuserapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var name: String,
    var follower: String,
    var following: String,
    var avatar: Int
) : Parcelable
