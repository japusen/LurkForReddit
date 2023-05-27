package com.example.lurkforreddit.data

import android.util.Log
import com.example.lurkforreddit.model.AccessToken
import com.example.lurkforreddit.network.ApiTokenService
import com.example.lurkforreddit.network.RedditApiService

interface RedditApiRepository {
    suspend fun initAccessToken()
    suspend fun getSubredditListing(subreddit: String): String
}

class DefaultRedditApiRepository(
    private val apiTokenService: ApiTokenService,
    private val redditApiService: RedditApiService
) : RedditApiRepository {

    private lateinit var accessToken: AccessToken
    private lateinit var tokenHeader: String


    override suspend fun initAccessToken() {
        val response = apiTokenService.getToken()
        if (response.isSuccessful) {
            accessToken = response.body()!!
            tokenHeader = "Bearer ${accessToken.accessToken}"
            Log.d("AccessToken", "Response:  ${response.body()}")
        } else {
            /* TODO: Proper error handling */
            throw Exception(response.errorBody()?.charStream()?.readText())
        }
    }

    override suspend fun getSubredditListing(subreddit: String): String {
        return redditApiService.getSubredditListing(tokenHeader, subreddit)
    }


}