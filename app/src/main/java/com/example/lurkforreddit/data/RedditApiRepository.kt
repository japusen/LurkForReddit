package com.example.lurkforreddit.data

import android.util.Log
import com.example.lurkforreddit.network.AccessToken
import com.example.lurkforreddit.network.ApiTokenService
import com.example.lurkforreddit.network.ListingData
import com.example.lurkforreddit.network.RedditApiService
import com.example.lurkforreddit.util.CommentSort
import com.example.lurkforreddit.util.ListingSort
import com.example.lurkforreddit.util.TopSort
import com.example.lurkforreddit.util.UserListing
import com.example.lurkforreddit.util.UserSort
import retrofit2.HttpException
import java.io.IOException

interface RedditApiRepository {
    suspend fun initAccessToken()

    suspend fun getListing(
        subreddit: String,
        listingSort: ListingSort,
        topSort: TopSort? = null
    ): ListingData

    suspend fun getDuplicates(
        subreddit: String,
        article: String
    ): ListingData

    suspend fun getComments(
        subreddit: String,
        article: String,
        sort: CommentSort
    ): ListingData

    suspend fun getUser(
        username: String,
        userListing: UserListing,
        userSort: UserSort,
        topSort: TopSort? = null
    ): ListingData
}

class DefaultRedditApiRepository(
    private val apiTokenService: ApiTokenService,
    private val redditApiService: RedditApiService
) : RedditApiRepository {

    private lateinit var accessToken: AccessToken
    private lateinit var tokenHeader: String


    override suspend fun initAccessToken() {
        try {
            val response = apiTokenService.getToken()
            if (response.isSuccessful) {
                accessToken = response.body()!!
                tokenHeader = "Bearer ${accessToken.accessToken}"
                Log.d("AccessToken", "Response:  ${response.body()}")
            } else {
                throw Exception(response.errorBody()?.charStream()?.readText())
            }
        } catch (e: IOException) {
            Log.d("TOKEN FAILURE", e.toString())
            tokenHeader = ""
        } catch (e: HttpException) {
            Log.d("TOKEN FAILURE", e.toString())
            tokenHeader = ""
        }
    }

    override suspend fun getListing(
        subreddit: String,
        listingSort: ListingSort,
        topSort: TopSort?
        ): ListingData {
        return redditApiService.getSubredditListing(
            tokenHeader,
            subreddit,
            listingSort.type,
            topSort?.type
        ).data
    }

    override suspend fun getComments(
        subreddit: String,
        article: String,
        sort: CommentSort
    ): ListingData {
        return redditApiService.getComments(
            tokenHeader,
            subreddit,
            article,
            sort.type
        )[1].data
    }

    override suspend fun getDuplicates(
        subreddit: String,
        article: String
    ): ListingData {
        return redditApiService.getDuplicates(
            tokenHeader,
            subreddit,
            article
        )[1].data
    }

    override suspend fun getUser(
        username: String,
        userListing: UserListing,
        userSort: UserSort,
        topSort: TopSort?
    ): ListingData {
        return redditApiService.getUser(
            tokenHeader,
            username,
            userListing.type,
            userSort.type,
            topSort?.type
        ).data
    }


}