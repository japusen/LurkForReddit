package com.example.lurkforreddit.data

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
    private val baseUrl = "https://www.reddit.com"

    private val json = Json { ignoreUnknownKeys = true }

    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: RedditApiService by lazy {
        retrofit.create(RedditApiService::class.java)
    }

    override val redditApiRepository: RedditApiRepository by lazy {
        DefaultRedditApiRepository(retrofitService)
    }
}