package com.example.lurkforreddit.network

import com.example.lurkforreddit.model.Thing
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path


interface RedditApiService {
    @GET("/r/{subreddit}")
    suspend fun getSubredditListing(
        @Header("Authorization") accessToken: String,
        @Path("subreddit") subreddit: String
    ): Thing
}