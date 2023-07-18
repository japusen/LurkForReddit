package com.example.lurkforreddit.network

import com.example.lurkforreddit.model.SearchResult
import kotlinx.serialization.json.JsonElement
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


interface RedditApiService {
    @GET("/r/{subreddit}/{sort}")
    suspend fun getSubredditPosts(
        @Header("Authorization") tokenHeader: String,
        @Path("subreddit") subreddit: String,
        @Path("sort") sort: String,
        @Query("t") topSort: String? = null,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
        @Query("raw_json") raw: Int = 1
    ): JsonElement

    @GET("/r/{subreddit}/duplicates/{article}")
    suspend fun getPostDuplicates(
        @Header("Authorization") tokenHeader: String,
        @Path("subreddit") subreddit: String,
        @Path("article") article: String,
        @Query("sort") sort: String,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
        @Query("raw_json") raw: Int = 1
    ): JsonElement

    @GET("/user/{username}/{data}")
    suspend fun getUserContent(
        @Header("Authorization") tokenHeader: String,
        @Path("username") username: String,
        @Path("data") contentType: String,
        @Query("sort") sort: String,
        @Query("t") topSort: String? = null,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
        @Query("raw_json") raw: Int = 1
    ): JsonElement

    @GET("/r/{subreddit}/comments/{article}")
    suspend fun getPostComments(
        @Header("Authorization") tokenHeader: String,
        @Path("subreddit") subreddit: String,
        @Path("article") article: String,
        @Query("sort") sort: String,
        @Query("raw_json") raw: Int = 1
    ): JsonElement

    @GET("/api/subreddit_autocomplete")
    suspend fun subredditAutoComplete(
        @Header("Authorization") tokenHeader: String,
        @Query("query") query: String,
        @Query("include_over_18") nsfw: Boolean = true,
        @Query("include_profiles") includeProfiles: Boolean = true,
        @Query("raw_json") raw: Int = 1
    ): JsonElement
}