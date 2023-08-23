package com.example.lurkforreddit.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.lurkforreddit.data.paging.DuplicatePostsPagingSource
import com.example.lurkforreddit.data.remote.RedditApiService
import com.example.lurkforreddit.data.repository.NetworkPageSize.NETWORK_PAGE_SIZE
import com.example.lurkforreddit.domain.model.Content
import com.example.lurkforreddit.domain.model.DuplicatesSort
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
                DuplicatePostsPagingSource(
                    service = redditApiService,
                    tokenHeader = tokenHeader,
                    subreddit = subreddit,
                    article = article,
                    sort = sort.value,
                )
            }
        ).flow
    }
}
