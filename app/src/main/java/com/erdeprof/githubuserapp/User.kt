package com.erdeprof.githubuserapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    // val name: String?,
    // val follower: Int?,
    // val following: Int?,
    val avatar: String?,
    val username: String?,
    // val location: String?,
    // val repository: Int?,
    // val company: String?
) : Parcelable
