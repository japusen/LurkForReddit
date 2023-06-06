package com.example.lurkforreddit.network

import com.example.lurkforreddit.util.DuplicatesSort
import kotlinx.serialization.json.JsonElement
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
    ): JsonElement

    @GET("/r/{subreddit}/duplicates/{article}")
    suspend fun getDuplicates(
        @Header("Authorization") accessToken: String,
        @Path("subreddit") subreddit: String,
        @Path("article") article: String,
        @Query("sort") sort: String,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null
    ): JsonElement

    @GET("/user/{username}/{data}")
    suspend fun getUser(
        @Header("Authorization") accessToken: String,
        @Path("username") username: String,
        @Path("data") data: String,
        @Query("sort") listingSort: String,
        @Query("t") topSort: String? = null,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null
    ): JsonElement

    @GET("/r/{subreddit}/comments/{article}")
    suspend fun getPostComments(
        @Header("Authorization") accessToken: String,
        @Path("subreddit") subreddit: String,
        @Path("article") article: String,
        @Query("sort") sort: String
    ): JsonElement
}