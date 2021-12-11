package com.erdeprof.githubuserapp

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    companion object {
        private const val TOKEN = "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"
    }

    @Headers("Authorization: token ${TOKEN}")
    @GET("search/users")
    fun getSearchUsers(
        @Query("q") q: String
    ): Call<UserSearchResponse>

    @Headers("Authorization: token ${TOKEN}")
    @GET("users/{username}")
    fun getDetailUsers(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @Headers("Authorization: token ${TOKEN}")
    @GET("users/{username}/followers")
    fun getFollowersUsers(
        @Path("username") username: String
    ): Call<UserFollowerResponse>
}