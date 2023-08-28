package com.example.lurkforreddit.di

import com.example.lurkforreddit.data.remote.AccessTokenService
import com.example.lurkforreddit.data.remote.RedditApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val tokenURL = "https://www.reddit.com"
    private const val apiURL = "https://oauth.reddit.com"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideTokenRetrofit(): AccessTokenService {
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(tokenURL)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideApiRetrofit(): RedditApiService {
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(apiURL)
            .build()
            .create()
    }
}