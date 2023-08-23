package com.example.lurkforreddit.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.lurkforreddit.data.pagingsource.ListingPagingSource
import com.example.lurkforreddit.data.remote.RedditApiService
import com.example.lurkforreddit.domain.model.Content
import com.example.lurkforreddit.domain.model.DuplicatesSort
import com.example.lurkforreddit.domain.model.PagingListing
import com.example.lurkforreddit.domain.repository.AccessTokenRepository
import com.example.lurkforreddit.domain.repository.DuplicatePostsRepository
import kotlinx.coroutines.flow.Flow

class DuplicatePostsRepositoryImpl(
    private val accessTokenRepository: AccessTokenRepository,
    private val redditApiService: RedditApiService
): DuplicatePostsRepository {

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

    companion object {
        const val NETWORK_PAGE_SIZE = 25
    }
}