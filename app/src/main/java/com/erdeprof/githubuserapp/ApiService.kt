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
}