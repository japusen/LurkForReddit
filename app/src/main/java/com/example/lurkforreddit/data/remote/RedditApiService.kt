package com.example.lurkforreddit.data.remote

import kotlinx.serialization.json.JsonElement
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


interface RedditApiService {
    @GET("/r/{subreddit}/{sort}")
    suspend fun fetchSubredditPosts(
        @Header("Authorization") tokenHeader: String,
        @Path("subreddit") subreddit: String,
        @Path("sort") sort: String,
        @Query("t") topSort: String? = null,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
        @Query("raw_json") raw: Int = 1
    ): JsonElement

    @GET("/r/{subreddit}/duplicates/{article}")
    suspend fun fetchPostDuplicates(
        @Header("Authorization") tokenHeader: String,
        @Path("subreddit") subreddit: String,
        @Path("article") article: String,
        @Query("sort") sort: String,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
        @Query("raw_json") raw: Int = 1
    ): JsonElement

    @GET("/user/{username}/{data}")
    suspend fun fetchUserContent(
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
    suspend fun fetchCommentThread(
        @Header("Authorization") tokenHeader: String,
        @Path("subreddit") subreddit: String,
        @Path("article") article: String,
        @Query("sort") sort: String,
        @Query("raw_json") raw: Int = 1
    ): JsonElement

    @GET("/api/subreddit_autocomplete")
    suspend fun fetchSearchResults(
        @Header("Authorization") tokenHeader: String,
        @Query("query") query: String,
        @Query("include_over_18") nsfw: Boolean = true,
        @Query("include_profiles") includeProfiles: Boolean = true,
        @Query("raw_json") raw: Int = 1
    ): JsonElement

    @GET("/api/morechildren")
    suspend fun fetchMoreComments(
        @Header("Authorization") tokenHeader: String,
        @Query("link_id") linkID: String,
        @Query("children") childrenIDs: String,
        @Query("sort") sort: String,
        @Query("limit_children") limit: Boolean = false,
        @Query("api_type") type: String = "json",
        @Query("raw_json") raw: Int = 1
    ): JsonElement
}