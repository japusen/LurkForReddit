package com.example.lurkforreddit.di

import com.example.lurkforreddit.data.remote.AccessTokenService
import com.example.lurkforreddit.data.remote.RedditApiService
import com.example.lurkforreddit.data.repository.AccessTokenRepositoryImpl
import com.example.lurkforreddit.data.repository.CommentThreadRepositoryImpl
import com.example.lurkforreddit.data.repository.DuplicatePostsRepositoryImpl
import com.example.lurkforreddit.data.repository.PostRepositoryImpl
import com.example.lurkforreddit.data.repository.ProfileRepositoryImpl
import com.example.lurkforreddit.data.repository.SearchResultsRepositoryImpl
import com.example.lurkforreddit.domain.repository.AccessTokenRepository
import com.example.lurkforreddit.domain.repository.CommentThreadRepository
import com.example.lurkforreddit.domain.repository.DuplicatePostsRepository
import com.example.lurkforreddit.domain.repository.PostRepository
import com.example.lurkforreddit.domain.repository.ProfileRepository
import com.example.lurkforreddit.domain.repository.SearchResultsRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val accessTokenRepository: AccessTokenRepository
    val postRepository: PostRepository
    val duplicatePostsRepository: DuplicatePostsRepository
    val commentThreadRepository: CommentThreadRepository
    val profileRepository: ProfileRepository
    val searchResultsRepository: SearchResultsRepository
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

    override val accessTokenRepository: AccessTokenRepository by lazy {
        AccessTokenRepositoryImpl(tokenRetrofitService)
    }

    override val postRepository: PostRepository by lazy {
        PostRepositoryImpl(accessTokenRepository, apiRetrofitService)
    }

    override val duplicatePostsRepository: DuplicatePostsRepository by lazy {
        DuplicatePostsRepositoryImpl(accessTokenRepository, apiRetrofitService)
    }

    override val commentThreadRepository: CommentThreadRepository by lazy {
        CommentThreadRepositoryImpl(accessTokenRepository, apiRetrofitService)
    }
    override val profileRepository: ProfileRepository by lazy {
        ProfileRepositoryImpl(accessTokenRepository, apiRetrofitService)
    }

    override val searchResultsRepository: SearchResultsRepository by lazy {
        SearchResultsRepositoryImpl(accessTokenRepository, apiRetrofitService)
    }
}