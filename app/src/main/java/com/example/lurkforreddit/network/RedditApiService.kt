package com.example.lurkforreddit.network

import com.example.lurkforreddit.model.Thing
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


interface RedditApiService {
    @GET("/r/{subreddit}/{sort}")
    suspend fun getSubredditListing(
        @Header("Authorization") accessToken: String,
        @Path("subreddit") subreddit: String,
        @Path("sort") sort: String,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
    ): Thing

    @GET("/r/{subreddit}/top")
    suspend fun getSubredditTopListing(
        @Header("Authorization") accessToken: String,
        @Path("subreddit") subreddit: String,
        @Query("t") sort: String,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
    ): Thing
}