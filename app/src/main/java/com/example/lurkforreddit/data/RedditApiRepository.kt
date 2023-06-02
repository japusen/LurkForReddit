package com.example.lurkforreddit.data

import android.util.Log
import com.example.lurkforreddit.network.AccessToken
import com.example.lurkforreddit.network.ApiTokenService
import com.example.lurkforreddit.network.PostListing
import com.example.lurkforreddit.network.ProfileCommentListing
import com.example.lurkforreddit.network.RedditApiService
import com.example.lurkforreddit.network.parsePostListing
import com.example.lurkforreddit.network.parseProfileCommentListing
import com.example.lurkforreddit.util.ListingSort
import com.example.lurkforreddit.util.TopSort
import com.example.lurkforreddit.util.UserListing
import com.example.lurkforreddit.util.UserSort
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
    ): PostListing

    suspend fun getDuplicates(
        subreddit: String,
        article: String
    ): PostListing

    suspend fun getUserSubmissions(
        username: String,
        sort: UserSort = UserSort.HOT,
        topType: TopSort? = null
    ): PostListing

    suspend fun getUserComments(
        username: String,
        sort: UserSort = UserSort.HOT,
        topType: TopSort? = null
    ): ProfileCommentListing

//    suspend fun getComments(
//        subreddit: String,
//        article: String,
//        sort: CommentSort
//    ): JsonElement
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
//                Log.d("AccessToken", "Response:  ${response.body()}")
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
    ): PostListing {

        val response = redditApiService.getSubredditListing(
            tokenHeader,
            subreddit,
            listingSort.value,
            topSort?.value
        )

        return parsePostListing(response)
    }

    override suspend fun getDuplicates(
        subreddit: String,
        article: String
    ): PostListing {

        val response = redditApiService.getDuplicates(
            tokenHeader,
            subreddit,
            article
        ).jsonArray[1]

        return parsePostListing(response)
    }

    override suspend fun getUserSubmissions(
        username: String,
        sort: UserSort,
        topType: TopSort?
    ): PostListing {

        val response = redditApiService.getUser(
            accessToken = tokenHeader,
            username = username,
            data = UserListing.SUBMITTED.value,
            listingSort = ListingSort.HOT.value,
            topSort = topType?.value
        )

        return parsePostListing(response)
    }

    override suspend fun getUserComments(
        username: String,
        sort: UserSort,
        topType: TopSort?
    ): ProfileCommentListing {

        val response = redditApiService.getUser(
            accessToken = tokenHeader,
            username = username,
            data = UserListing.COMMENTS.value,
            listingSort = ListingSort.HOT.value,
            topSort = topType?.value
        ).jsonObject

        return parseProfileCommentListing(response)
    }

}