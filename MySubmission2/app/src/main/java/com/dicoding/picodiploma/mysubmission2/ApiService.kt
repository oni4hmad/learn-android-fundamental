package com.dicoding.picodiploma.mysubmission2

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getUsersSearch(
        @Query("q") query: String
    ): Call<SearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserInfo(
        @Path("username") login: String
    ): Call<UserInfo>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getUserFollowers(
        @Path("username") login: String
    ): Call<List<UserFollower>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getUserFollowing(
        @Path("username") login: String
    ): Call<List<UserFollowing>>
}