package com.example.lurkforreddit.data

import android.util.Log
import com.example.lurkforreddit.model.AccessToken
import com.example.lurkforreddit.network.ApiTokenService
import com.example.lurkforreddit.network.RedditApiService

interface RedditApiRepository {
    suspend fun initAccessToken()
}

class DefaultRedditApiRepository(
    private val apiTokenService: ApiTokenService,
    private val redditApiService: RedditApiService
) : RedditApiRepository {

    private lateinit var accessToken: AccessToken

    override suspend fun initAccessToken() {
        val response = apiTokenService.getToken()
        if (response.isSuccessful) {
            accessToken = response.body()!!
            Log.d("AccessToken", "Response:  ${response.body()}")
        } else {
            throw Exception(response.errorBody()?.charStream()?.readText())
        }
    }


}