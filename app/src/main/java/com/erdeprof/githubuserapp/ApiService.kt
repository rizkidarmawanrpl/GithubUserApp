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