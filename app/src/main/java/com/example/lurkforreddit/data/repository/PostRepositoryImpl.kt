package com.example.lurkforreddit.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.lurkforreddit.data.local.PostHistoryDatabase
import com.example.lurkforreddit.data.mappers.toPost
import com.example.lurkforreddit.data.mappers.toPostEntity
import com.example.lurkforreddit.data.paging.PostPagingSource
import com.example.lurkforreddit.data.remote.RedditApiService
import com.example.lurkforreddit.data.repository.NetworkPageSize.NETWORK_PAGE_SIZE
import com.example.lurkforreddit.domain.model.Content
import com.example.lurkforreddit.domain.model.ListingSort
import com.example.lurkforreddit.domain.model.Post
import com.example.lurkforreddit.domain.model.TopSort
import com.example.lurkforreddit.domain.repository.AccessTokenRepository
import com.example.lurkforreddit.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val accessTokenRepository: AccessTokenRepository,
    private val redditApiService: RedditApiService,
    database: PostHistoryDatabase
): PostRepository {

    private val dao = database.dao

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
    ): Flow<PagingData<Post>> {

        val tokenHeader = accessTokenRepository.getAccessToken()

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PostPagingSource(
                    service = redditApiService,
                    tokenHeader = tokenHeader,
                    subreddit = subreddit,
                    sort = sort.value,
                    topSort = topSort?.value
                )
            }
        ).flow
    }

    override fun getPostHistory(): Flow<List<Post>> {
        return dao.getAllPosts()
            .onEmpty {
                emit(listOf())
            }
            .map { list ->
                list.map { postEntity -> postEntity.toPost() }
            }
    }

    override suspend fun savePostToHistory(post: Post) {
        dao.insert(post.toPostEntity())
    }

    override suspend fun clearPostHistory() {
        dao.clearPostHistory()
    }


}