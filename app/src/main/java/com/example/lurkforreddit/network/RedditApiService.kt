package com.example.lurkforreddit.network

import com.example.lurkforreddit.model.Listing
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
        @Query("t") topSort: String? = null,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null
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
        @Query("before") before: String? = null
    ): List<Listing>

    @GET("/user/{username}/{data}")
    suspend fun getUser(
        @Header("Authorization") accessToken: String,
        @Path("username") username: String,
        @Path("data") data: String,
        @Query("sort") listingSort: String,
        @Query("t") topSort: String? = null,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null
    ): Listing
}