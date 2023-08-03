package com.example.lurkforreddit.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.lurkforreddit.data.RedditApiRepository.Companion.NETWORK_PAGE_SIZE
import com.example.lurkforreddit.model.AccessToken
import com.example.lurkforreddit.model.Comment
import com.example.lurkforreddit.model.Content
import com.example.lurkforreddit.model.More
import com.example.lurkforreddit.model.Post
import com.example.lurkforreddit.model.SearchResult
import com.example.lurkforreddit.network.AccessTokenService
import com.example.lurkforreddit.network.RedditApiService
import com.example.lurkforreddit.network.parseMoreComments
import com.example.lurkforreddit.network.parsePostComments
import com.example.lurkforreddit.network.parsePostListing
import com.example.lurkforreddit.network.parseSearchResults
import com.example.lurkforreddit.util.CommentSort
import com.example.lurkforreddit.util.DuplicatesSort
import com.example.lurkforreddit.util.ListingSort
import com.example.lurkforreddit.util.PagingListing
import com.example.lurkforreddit.util.TopSort
import com.example.lurkforreddit.util.UserListingSort
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.jsonArray
import retrofit2.HttpException
import java.io.IOException

interface RedditApiRepository {
    suspend fun initAccessToken()

    suspend fun getPosts(
        subreddit: String,
        sort: ListingSort,
        topSort: TopSort? = null
    ): Flow<PagingData<Content>>

    suspend fun getPostDuplicates(
        subreddit: String,
        article: String,
        sort: DuplicatesSort
    ): Flow<PagingData<Content>>

    suspend fun getUserSubmissions(
        username: String,
        sort: UserListingSort,
        topSort: TopSort? = null
    ): Flow<PagingData<Content>>

    suspend fun getUserComments(
        username: String,
        sort: UserListingSort,
        topSort: TopSort? = null
    ): Flow<PagingData<Content>>

    suspend fun getPostComments(
        subreddit: String,
        article: String,
        sort: CommentSort
    ): Pair<Post, Pair<List<Comment>, More?>>

    suspend fun subredditAutoComplete(
        query: String,
    ): List<SearchResult>

    suspend fun getMoreComments(
        linkID: String,
        childrenIDs: String,
        sort: CommentSort,
    ): List<Comment>

    companion object {
        const val NETWORK_PAGE_SIZE = 25
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
                //Log.d("AccessToken", "Response:  ${response.body()}")
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


    override suspend fun getPosts(
        subreddit: String,
        sort: ListingSort,
        topSort: TopSort?
    ): Flow<PagingData<Content>> {

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ListingPagingSource(
                    listingType = PagingListing.POSTS,
                    service = redditApiService,
                    tokenHeader = tokenHeader,
                    subreddit = subreddit,
                    sort = sort.value,
                    topSort = topSort?.value
                )
            }
        ).flow
    }


    override suspend fun getPostDuplicates(
        subreddit: String,
        article: String,
        sort: DuplicatesSort
    ): Flow<PagingData<Content>> {

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ListingPagingSource(
                    listingType = PagingListing.DUPLICATES,
                    service = redditApiService,
                    tokenHeader = tokenHeader,
                    subreddit = subreddit,
                    sort = sort.value,
                    article = article
                )
            }
        ).flow
    }


    override suspend fun getUserSubmissions(
        username: String,
        sort: UserListingSort,
        topSort: TopSort?
    ): Flow<PagingData<Content>> {

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ListingPagingSource(
                    listingType = PagingListing.USERSUBMISSIONS,
                    service = redditApiService,
                    tokenHeader = tokenHeader,
                    username = username,
                    sort = sort.value,
                    topSort = topSort?.value
                )
            }
        ).flow
    }


    override suspend fun getUserComments(
        username: String,
        sort: UserListingSort,
        topSort: TopSort?
    ): Flow<PagingData<Content>> {

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ListingPagingSource(
                    listingType = PagingListing.USERCOMMENTS,
                    service = redditApiService,
                    tokenHeader = tokenHeader,
                    username = username,
                    sort = sort.value,
                    topSort = topSort?.value
                )
            }
        ).flow
    }


    override suspend fun getPostComments(
        subreddit: String,
        article: String,
        sort: CommentSort
    ): Pair<Post, Pair<List<Comment>, More?>> {

        val response = redditApiService.getPostComments(
            tokenHeader,
            subreddit,
            article,
            sort.value
        )

        val listing = parsePostListing(response.jsonArray[0])
        val post = listing.children[0]
        val comments = parsePostComments(response.jsonArray[1])

        return Pair(post, comments)
    }

    override suspend fun subredditAutoComplete(query: String): List<SearchResult> {
        val response = redditApiService.subredditAutoComplete(
            tokenHeader,
            query
        )

        val results = parseSearchResults(response)
        return results ?: listOf()
    }

    override suspend fun getMoreComments(
        linkID: String,
        childrenIDs: String,
        sort: CommentSort
    ): List<Comment> {
        val response = redditApiService.getMoreComments(
            tokenHeader,
            "t3_$linkID",
            childrenIDs,
            sort.value
        )
        return parseMoreComments(response, "t3_$linkID")
    }
}