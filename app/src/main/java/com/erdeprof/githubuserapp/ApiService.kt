package com.erdeprof.githubuserapp

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("Authorization: token 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")
    @GET("search/users")
    fun getSearchUsers(
        @Query("q") q: String
    ): Call<UserSearchResponse>
}