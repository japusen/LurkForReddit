package com.example.lurkforreddit.data

import android.util.Log
import com.example.lurkforreddit.network.AccessToken
import com.example.lurkforreddit.network.ApiTokenService
import com.example.lurkforreddit.network.RedditApiService
import com.example.lurkforreddit.util.ListingSort
import com.example.lurkforreddit.util.TopSort
import com.example.lurkforreddit.util.UserListing
import com.example.lurkforreddit.util.UserSort
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import retrofit2.HttpException
import java.io.IOException

interface RedditApiRepository {
    suspend fun initAccessToken()

    suspend fun getListing(
        subreddit: String,
        listingSort: ListingSort,
        topSort: TopSort? = null
    ): JsonElement

    suspend fun getDuplicates(
        subreddit: String,
        article: String
    ): JsonElement

//    suspend fun getComments(
//        subreddit: String,
//        article: String,
//        sort: CommentSort
//    ): JsonElement

    suspend fun getUserSubmissions(
        username: String,
        sort: UserSort = UserSort.HOT,
        topType: TopSort? = null
    ): JsonElement

    suspend fun getUserComments(
        username: String,
        sort: UserSort = UserSort.HOT,
        topType: TopSort? = null
    ): JsonElement
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
        ): JsonObject {
        return redditApiService.getSubredditListing(
            tokenHeader,
            subreddit,
            listingSort.value,
            topSort?.value
        ).jsonObject
    }

//    override suspend fun getComments(
//        subreddit: String,
//        article: String,
//        sort: CommentSort
//    ): JsonElement {
//        return redditApiService.getComments(
//            tokenHeader,
//            subreddit,
//            article,
//            sort.type
//        ).jsonArray[1].jsonObject
//    }

    override suspend fun getDuplicates(
        subreddit: String,
        article: String
    ): JsonElement {
        return redditApiService.getDuplicates(
            tokenHeader,
            subreddit,
            article
        ).jsonArray[1].jsonObject
    }

    override suspend fun getUserSubmissions(
        username: String,
        sort: UserSort,
        topType: TopSort?
    ): JsonElement {
        return redditApiService.getUser(
            accessToken = tokenHeader,
            username = username,
            data = UserListing.SUBMITTED.value,
            listingSort = ListingSort.HOT.value,
            topSort = topType?.value
        ).jsonObject
    }

    override suspend fun getUserComments(
        username: String,
        sort: UserSort,
        topType: TopSort?
    ): JsonElement {
        return redditApiService.getUser(
            accessToken = tokenHeader,
            username = username,
            data = UserListing.COMMENTS.value,
            listingSort = ListingSort.HOT.value,
            topSort = topType?.value
        ).jsonObject
    }


}