package com.example.lurkforreddit.network

import com.example.lurkforreddit.model.Listing
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
    ): Listing

    @GET("/r/{subreddit}/top")
    suspend fun getSubredditTopListing(
        @Header("Authorization") accessToken: String,
        @Path("subreddit") subreddit: String,
        @Query("t") sort: String,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
    ): Listing

    @GET("/r/{subreddit}/comments/{article}")
    suspend fun getComments(
        @Header("Authorization") accessToken: String,
        @Path("subreddit") subreddit: String,
        @Path("article") article: String,
        @Query("sort") sort: String
    ): List<Listing>

    @GET("/r/{subreddit}/duplicates/{article}")
    suspend fun getDuplicates(
        @Header("Authorization") accessToken: String,
        @Path("subreddit") subreddit: String,
        @Path("article") article: String,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
    ): List<Listing>
}