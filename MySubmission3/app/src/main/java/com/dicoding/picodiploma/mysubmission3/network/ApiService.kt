package com.dicoding.picodiploma.mysubmission3.network

import com.dicoding.picodiploma.mysubmission3.BuildConfig
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getUsersSearch(
        @Query("q") query: String
    ): Call<SearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getUserInfo(
        @Path("username") login: String
    ): Call<UserInfo>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getUserFollowers(
        @Path("username") login: String
    ): Call<List<UserFollower>>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getUserFollowing(
        @Path("username") login: String
    ): Call<List<UserFollowing>>
}