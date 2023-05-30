package com.example.lurkforreddit.data

import android.util.Log
import com.example.lurkforreddit.model.AccessToken
import com.example.lurkforreddit.model.CommentSort
import com.example.lurkforreddit.model.Listing
import com.example.lurkforreddit.model.ListingData
import com.example.lurkforreddit.model.ListingSort
import com.example.lurkforreddit.model.Thing
import com.example.lurkforreddit.model.ListingTopSort
import com.example.lurkforreddit.network.ApiTokenService
import com.example.lurkforreddit.network.RedditApiService

interface RedditApiRepository {
    suspend fun initAccessToken()
    suspend fun getListing(subreddit: String, sort: ListingSort): ListingData
    suspend fun getTopListing(subreddit: String, sort: ListingTopSort): ListingData

    suspend fun getComments(subreddit: String, article: String, sort: CommentSort): ListingData
    suspend fun getDuplicates(subreddit: String, article: String): ListingData
}

class DefaultRedditApiRepository(
    private val apiTokenService: ApiTokenService,
    private val redditApiService: RedditApiService
) : RedditApiRepository {

    private lateinit var accessToken: AccessToken
    private lateinit var tokenHeader: String


    override suspend fun initAccessToken() {
        /* TODO: Proper error handling */
        val response = apiTokenService.getToken()
        if (response.isSuccessful) {
            accessToken = response.body()!!
            tokenHeader = "Bearer ${accessToken.accessToken}"
            Log.d("AccessToken", "Response:  ${response.body()}")
        } else {
            throw Exception(response.errorBody()?.charStream()?.readText())
        }
    }

    override suspend fun getListing(
        subreddit: String,
        sort: ListingSort
    ): ListingData {
        return redditApiService.getSubredditListing(tokenHeader, subreddit, sort.type).data
    }

    override suspend fun getTopListing(
        subreddit: String,
        sort: ListingTopSort
    ): ListingData {
        return redditApiService.getSubredditTopListing(tokenHeader, subreddit, sort.type).data
    }

    override suspend fun getComments(
        subreddit: String,
        article: String,
        sort: CommentSort
    ): ListingData {
        return redditApiService.getComments(tokenHeader, subreddit, article, sort.type)[1].data
    }

    override suspend fun getDuplicates(
        subreddit: String,
        article: String
    ): ListingData {
        return redditApiService.getDuplicates(tokenHeader, subreddit, article)[1].data
    }
}