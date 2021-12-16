package com.erdeprof.githubuserapp

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
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

    companion object {
        private const val TOKEN = "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"
    }
}