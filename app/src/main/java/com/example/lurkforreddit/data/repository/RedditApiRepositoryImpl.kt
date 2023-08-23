package com.example.lurkforreddit.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.lurkforreddit.data.pagingsource.ListingPagingSource
import com.example.lurkforreddit.data.json.parseMoreComments
import com.example.lurkforreddit.data.json.parsePostComments
import com.example.lurkforreddit.data.json.parsePostListing
import com.example.lurkforreddit.data.json.parseSearchResults
import com.example.lurkforreddit.data.remote.RedditApiService
import com.example.lurkforreddit.data.remote.model.PostDto
import com.example.lurkforreddit.data.remote.model.SearchResultDto
import com.example.lurkforreddit.domain.model.CommentSort
import com.example.lurkforreddit.domain.model.CommentThreadItem
import com.example.lurkforreddit.domain.model.Content
import com.example.lurkforreddit.domain.model.DuplicatesSort
import com.example.lurkforreddit.domain.model.ListingSort
import com.example.lurkforreddit.domain.model.PagingListing
import com.example.lurkforreddit.domain.model.TopSort
import com.example.lurkforreddit.domain.model.UserListingSort
import com.example.lurkforreddit.domain.repository.AccessTokenRepository
import com.example.lurkforreddit.domain.repository.RedditApiRepository
import com.example.lurkforreddit.domain.repository.RedditApiRepository.Companion.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.jsonArray


class RedditApiRepositoryImpl(
    private val accessTokenRepository: AccessTokenRepository,
    private val redditApiService: RedditApiService
) : RedditApiRepository {

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

        val tokenHeader = accessTokenRepository.getAccessToken()

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
    override suspend fun getDuplicatePosts(
        subreddit: String,
        article: String,
        sort: DuplicatesSort
    ): Flow<PagingData<Content>> {

        val tokenHeader = accessTokenRepository.getAccessToken()

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

        val tokenHeader = accessTokenRepository.getAccessToken()

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

        val tokenHeader = accessTokenRepository.getAccessToken()

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
    override suspend fun getCommentThread(
        subreddit: String,
        article: String,
        sort: CommentSort
    ): Pair<PostDto, MutableList<CommentThreadItem>> {

        val tokenHeader = accessTokenRepository.getAccessToken()

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
    override suspend fun getSearchResults(query: String): List<SearchResultDto> {

        val tokenHeader = accessTokenRepository.getAccessToken()

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

        val tokenHeader = accessTokenRepository.getAccessToken()

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