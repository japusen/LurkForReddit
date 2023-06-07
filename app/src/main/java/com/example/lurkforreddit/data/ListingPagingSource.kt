package com.example.lurkforreddit.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.lurkforreddit.network.Content
import com.example.lurkforreddit.network.RedditApiService
import com.example.lurkforreddit.network.parsePostListing
import com.example.lurkforreddit.util.ListingSort
import com.example.lurkforreddit.util.TopSort
import retrofit2.HttpException
import java.io.IOException


class ListingPagingSource(
    private val service: RedditApiService,
    private val tokenHeader: String,
    private val subreddit: String,
    private val sort: ListingSort,
    private val topSort: TopSort? = null
) : PagingSource<String, Content>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, Content> {
        val after = params.key
        return try {
            val response = service.getSubredditListing(
                tokenHeader,
                subreddit,
                sort.value,
                topSort?.value,
                after
            )
            val listing = parsePostListing(response)
            LoadResult.Page(
                data = listing.children,
                prevKey = after,
                nextKey = listing.after
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<String, Content>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
                ?: state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }
}