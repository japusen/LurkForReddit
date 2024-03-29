package com.example.lurkforreddit.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.lurkforreddit.data.json.parsePostListing
import com.example.lurkforreddit.data.remote.RedditApiService
import com.example.lurkforreddit.domain.model.Post
import com.example.lurkforreddit.domain.model.PostListing
import retrofit2.HttpException
import java.io.IOException


class PostPagingSource(
    private val service: RedditApiService,
    private val tokenHeader: String,
    private val subreddit: String,
    private val sort: String,
    private val topSort: String?
) : PagingSource<String, Post>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, Post> {
        val after = params.key
        return try {

            val listing = getPosts(after)

            LoadResult.Page(
                data = listing.children,
                prevKey = after,
                nextKey = listing.after
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<String, Post>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
                ?: state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }

    private suspend fun getPosts(after: String?): PostListing {
        return parsePostListing(
            service.fetchSubredditPosts(
                tokenHeader = tokenHeader,
                subreddit = subreddit,
                sort = sort,
                topSort = topSort,
                after = after
            )
        )
    }
}