package com.example.lurkforreddit.data

import com.example.lurkforreddit.network.ApiTokenService
import com.example.lurkforreddit.network.RedditApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val redditApiRepository: RedditApiRepository
}

class DefaultAppContainer: AppContainer {
    private val tokenURL = "https://www.reddit.com"
    private val apiURL = "https://oauth.reddit.com"

    private val json = Json { ignoreUnknownKeys = true }

    @OptIn(ExperimentalSerializationApi::class)
    private val tokenRetrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(tokenURL)
        .build()

    @OptIn(ExperimentalSerializationApi::class)
    private val apiRetrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(apiURL)
        .build()

    private val tokenRetrofitService: ApiTokenService by lazy {
        tokenRetrofit.create(ApiTokenService::class.java)
    }

    private val apiRetrofitService: RedditApiService by lazy {
        apiRetrofit.create(RedditApiService::class.java)
    }

    override val redditApiRepository: RedditApiRepository by lazy {
        DefaultRedditApiRepository(tokenRetrofitService, apiRetrofitService)
    }
}