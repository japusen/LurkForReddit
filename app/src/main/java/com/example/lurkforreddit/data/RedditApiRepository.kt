package com.example.lurkforreddit.data

import android.util.Log
import com.example.lurkforreddit.model.AccessToken
import com.example.lurkforreddit.model.ListingSort
import com.example.lurkforreddit.model.Thing
import com.example.lurkforreddit.model.TopSort
import com.example.lurkforreddit.network.ApiTokenService
import com.example.lurkforreddit.network.RedditApiService

interface RedditApiRepository {
    suspend fun initAccessToken()
    suspend fun getListing(subreddit: String, sort: ListingSort): Thing
    suspend fun getTopListing(subreddit: String, sort: TopSort): Thing
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

    override suspend fun getListing(subreddit: String, sort: ListingSort
    ): Thing {
        return redditApiService.getSubredditListing(tokenHeader, subreddit, sort.type)
    }

    override suspend fun getTopListing(subreddit: String, sort: TopSort
    ): Thing {
        return redditApiService.getSubredditTopListing(tokenHeader, subreddit, sort.type)
    }
}