package com.example.lurkforreddit.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.lurkforreddit.data.RedditApiRepository.Companion.NETWORK_PAGE_SIZE
import com.example.lurkforreddit.network.AccessToken
import com.example.lurkforreddit.network.AccessTokenService
import com.example.lurkforreddit.network.CommentApi
import com.example.lurkforreddit.network.Content
import com.example.lurkforreddit.network.Listing
import com.example.lurkforreddit.network.MoreApi
import com.example.lurkforreddit.network.PostListing
import com.example.lurkforreddit.network.ProfileCommentListing
import com.example.lurkforreddit.network.RedditApiService
import com.example.lurkforreddit.network.parseComments
import com.example.lurkforreddit.network.parsePostListing
import com.example.lurkforreddit.network.parseProfileCommentListing
import com.example.lurkforreddit.util.CommentSort
import com.example.lurkforreddit.util.DuplicatesSort
import com.example.lurkforreddit.util.ListingSort
import com.example.lurkforreddit.util.TopSort
import com.example.lurkforreddit.util.UserListingSort
import com.example.lurkforreddit.util.UserListingType
import kotlinx.coroutines.flow.Flow
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
    ): Listing

    suspend fun getListingStream(
        subreddit: String,
        listingSort: ListingSort,
        topSort: TopSort?
    ): Flow<PagingData<Content>>

    suspend fun getDuplicates(
        subreddit: String,
        article: String,
        sort: DuplicatesSort
    ): Listing

    suspend fun getUserSubmissions(
        username: String,
        sort: UserListingSort,
        topType: TopSort? = null
    ): Listing

    suspend fun getUserComments(
        username: String,
        sort: UserListingSort,
        topType: TopSort? = null
    ): Listing

    suspend fun getPostComments(
        subreddit: String,
        article: String,
        sort: CommentSort
    ): Pair<List<CommentApi>, MoreApi?>

    companion object {
        const val NETWORK_PAGE_SIZE = 50
    }
}

class DefaultRedditApiRepository(
    private val accessTokenService: AccessTokenService,
    private val redditApiService: RedditApiService
) : RedditApiRepository {

    private lateinit var accessToken: AccessToken
    private lateinit var tokenHeader: String

    override suspend fun initAccessToken() {
        try {
            val response = accessTokenService.getToken()
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

    override suspend fun getListingStream(
        subreddit: String,
        listingSort: ListingSort,
        topSort: TopSort?
    ): Flow<PagingData<Content>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ListingPagingSource(
                    redditApiService,
                    tokenHeader,
                    subreddit,
                    listingSort,
                    topSort
                )
            }
        ).flow
    }


    override suspend fun getDuplicates(
        subreddit: String,
        article: String,
        sort: DuplicatesSort
    ): PostListing {

        val response = redditApiService.getDuplicates(
            tokenHeader,
            subreddit,
            article,
            sort.value
        ).jsonArray[1]

        return parsePostListing(response)
    }

    override suspend fun getUserSubmissions(
        username: String,
        sort: UserListingSort,
        topType: TopSort?
    ): PostListing {

        val response = redditApiService.getUser(
            accessToken = tokenHeader,
            username = username,
            data = UserListingType.SUBMITTED.value,
            listingSort = sort.value,
            topSort = topType?.value
        )

        return parsePostListing(response)
    }

    override suspend fun getUserComments(
        username: String,
        sort: UserListingSort,
        topType: TopSort?
    ): ProfileCommentListing {

        val response = redditApiService.getUser(
            accessToken = tokenHeader,
            username = username,
            data = UserListingType.COMMENTS.value,
            listingSort = sort.value,
            topSort = topType?.value
        ).jsonObject

        return parseProfileCommentListing(response)
    }

    override suspend fun getPostComments(
        subreddit: String,
        article: String,
        sort: CommentSort
    ): Pair<List<CommentApi>, MoreApi?> {

        val response = redditApiService.getPostComments(
            tokenHeader,
            subreddit,
            article,
            sort.value
        ).jsonArray[1]

        return parseComments(response)
    }

}