package com.example.lurkforreddit.di

import com.example.lurkforreddit.data.remote.AccessTokenService
import com.example.lurkforreddit.data.remote.RedditApiService
import com.example.lurkforreddit.data.repository.DefaultRedditApiRepository
import com.example.lurkforreddit.domain.repository.RedditApiRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val redditApiRepository: RedditApiRepository
}

class DefaultAppContainer : AppContainer {
    private val tokenURL = "https://www.reddit.com"
    private val apiURL = "https://oauth.reddit.com"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val tokenRetrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(tokenURL)
        .build()

    private val apiRetrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(apiURL)
        .build()

    private val tokenRetrofitService: AccessTokenService by lazy {
        tokenRetrofit.create(AccessTokenService::class.java)
    }

    private val apiRetrofitService: RedditApiService by lazy {
        apiRetrofit.create(RedditApiService::class.java)
    }

    override val redditApiRepository: RedditApiRepository by lazy {
        DefaultRedditApiRepository(tokenRetrofitService, apiRetrofitService)
    }
}