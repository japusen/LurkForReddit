package com.example.lurkforreddit.data

import android.util.Log
import com.example.lurkforreddit.network.AccessToken
import com.example.lurkforreddit.network.ApiTokenService
import com.example.lurkforreddit.network.PostApi
import com.example.lurkforreddit.network.PostListing
import com.example.lurkforreddit.network.RedditApiService
import com.example.lurkforreddit.util.ListingSort
import com.example.lurkforreddit.util.TopSort
import com.example.lurkforreddit.util.UserListing
import com.example.lurkforreddit.util.UserSort
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException
import java.io.IOException

interface RedditApiRepository {
    suspend fun initAccessToken()

    suspend fun getListing(
        subreddit: String,
        listingSort: ListingSort,
        topSort: TopSort? = null
    ): String

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

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val blank = json.parseToJsonElement("{}")


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
        ): String {

        val root = redditApiService.getSubredditListing(
            tokenHeader,
            subreddit,
            listingSort.value,
            topSort?.value
        )


        val listing = root.jsonObject.getOrDefault("data", blank)
        val after =  listing.jsonObject["after"]?.jsonPrimitive?.contentOrNull
        val before =  listing.jsonObject["before"]?.jsonPrimitive?.contentOrNull
        val dist = listing.jsonObject["dist"]?.jsonPrimitive?.content?.toInt()
        val children = listing.jsonObject.getOrDefault("children", blank)

        val posts = children.jsonArray.map {
            json.decodeFromJsonElement(
                PostApi.serializer(),
                it.jsonObject.getOrDefault("data", blank)
            )
        }

        return PostListing(after, before, dist, posts).toString()
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