package com.example.lurkforreddit.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.lurkforreddit.data.pagingsource.ListingPagingSource
import com.example.lurkforreddit.data.remote.RedditApiService
import com.example.lurkforreddit.domain.model.Content
import com.example.lurkforreddit.domain.model.ListingSort
import com.example.lurkforreddit.domain.model.PagingListing
import com.example.lurkforreddit.domain.model.TopSort
import com.example.lurkforreddit.domain.repository.AccessTokenRepository
import com.example.lurkforreddit.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class PostRepositoryImpl(
    private val accessTokenRepository: AccessTokenRepository,
    private val redditApiService: RedditApiService
): PostRepository {

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

    companion object {
        const val NETWORK_PAGE_SIZE = 25
    }
}