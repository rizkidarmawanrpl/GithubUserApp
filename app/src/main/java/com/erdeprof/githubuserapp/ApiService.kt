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
    ): Call<List<UserFollowerResponseItem>>

    @Headers("Authorization: token ${TOKEN}")
    @GET("users/{username}/following")
    fun getFollowingUsers(
        @Path("username") username: String
    ): Call<List<UserFollowerResponseItem>>
}