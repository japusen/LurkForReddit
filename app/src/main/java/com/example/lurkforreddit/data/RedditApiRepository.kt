package com.example.lurkforreddit.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.lurkforreddit.data.RedditApiRepository.Companion.NETWORK_PAGE_SIZE
import com.example.lurkforreddit.model.AccessToken
import com.example.lurkforreddit.model.CommentSort
import com.example.lurkforreddit.model.CommentThreadItem
import com.example.lurkforreddit.model.Content
import com.example.lurkforreddit.model.DuplicatesSort
import com.example.lurkforreddit.model.ListingSort
import com.example.lurkforreddit.model.PagingListing
import com.example.lurkforreddit.model.Post
import com.example.lurkforreddit.model.SearchResult
import com.example.lurkforreddit.model.TopSort
import com.example.lurkforreddit.model.UserListingSort
import com.example.lurkforreddit.network.AccessTokenService
import com.example.lurkforreddit.network.RedditApiService
import com.example.lurkforreddit.network.parseMoreComments
import com.example.lurkforreddit.network.parsePostComments
import com.example.lurkforreddit.network.parsePostListing
import com.example.lurkforreddit.network.parseSearchResults
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
    ): Pair<Post, MutableList<CommentThreadItem>>

    suspend fun subredditAutoComplete(
        query: String,
    ): List<SearchResult>

    suspend fun getMoreComments(
        linkID: String,
        parentID: String,
        childrenIDs: String,
        sort: CommentSort,
    ): MutableList<CommentThreadItem>

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

    /**
     * Initializes the access token for api calls
     * @throws Exception if network request fails
     */
    override suspend fun initAccessToken() {
        try {
            val response = accessTokenService.getToken()
            if (response.isSuccessful) {
                accessToken = response.body()!!
                tokenHeader = "Bearer ${accessToken.accessToken}"
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

    /**
     * Network call to fetch posts from network
     * @param subreddit the name of the subreddit to load posts from
     * @param sort the type of sort (hot, rising, new, top)
     * @param topSort the time frame if the sort is top (hour, day, week, month, year, all)
     * @return Flow of PagingData Content
     */
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

    /**
     * Network call to fetch duplicate posts
     * @param subreddit the name of the subreddit
     * @param article the id of the article (post)
     * @param sort the type of sort (number of comments, new)
     * @return Flow of PagingData Content
     */
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

    /**
     * Network call to fetch user submissions
     * @param username the name of the user
     * @param sort the type of sort (number of comments, new)
     * @param topSort the time frame if the sort is top (hour, day, week, month, year, all)
     * @return Flow of PagingData Content
     */
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


    /**
     * Network call to fetch user comments
     * @param username the name of the user
     * @param sort the type of sort (number of comments, new)
     * @param topSort the time frame if the sort is top (hour, day, week, month, year, all)
     * @return Flow of PagingData Content
     */
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


    /**
     * Network call to fetch comments of a specific post
     * @param subreddit the name of the subreddit
     * @param article the id of the article (post)
     * @param sort the type of sort (number of comments, new)
     * @return Post and list of Comments
     */
    override suspend fun getPostComments(
        subreddit: String,
        article: String,
        sort: CommentSort
    ): Pair<Post, MutableList<CommentThreadItem>> {

        val response = redditApiService.getPostComments(
            tokenHeader,
            subreddit,
            article,
            sort.value
        )

        val listing = parsePostListing(response.jsonArray[0])
        val post = listing.children[0]

        val commentThread = mutableListOf<CommentThreadItem>()
        parsePostComments(response.jsonArray[1], commentThread)

        return Pair(post, commentThread)
    }

    /**
     * Network call to get subreddit and usernames similar to the search query
     * @param query the query to perform search on
     * @return a list of SearchResults for the given query
     */
    override suspend fun subredditAutoComplete(query: String): List<SearchResult> {
        val response = redditApiService.subredditAutoComplete(
            tokenHeader,
            query
        )

        return parseSearchResults(response)
    }

    /**
     * Network call to fetch more comments
     * @param linkID the id of the post
     * @param childrenIDs a comma delimited list of comment ids to fetch
     * @param sort the type of sort (best, top, new, controversial, q&a)
     * @return a list of comments requested
     */
    override suspend fun getMoreComments(
        linkID: String,
        parentID: String,
        childrenIDs: String,
        sort: CommentSort
    ): MutableList<CommentThreadItem> {
        val response = redditApiService.getMoreComments(
            tokenHeader,
            "t3_$linkID",
            childrenIDs,
            sort.value
        )
        val commentThread = mutableListOf<CommentThreadItem>()
        parseMoreComments(response, commentThread)
        return commentThread
    }
}