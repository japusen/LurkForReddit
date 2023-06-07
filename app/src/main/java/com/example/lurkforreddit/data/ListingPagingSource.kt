package com.example.lurkforreddit.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.lurkforreddit.network.Content
import com.example.lurkforreddit.network.Listing
import com.example.lurkforreddit.network.RedditApiService
import com.example.lurkforreddit.network.parsePostListing
import com.example.lurkforreddit.network.parseProfileCommentListing
import com.example.lurkforreddit.util.PagingListing
import com.example.lurkforreddit.util.UserListingType
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import retrofit2.HttpException
import java.io.IOException


class ListingPagingSource(
    private val listingType: PagingListing,
    private val service: RedditApiService,
    private val tokenHeader: String,
    private val sort: String,
    private val subreddit: String? = null,
    private val username: String? = null,
    private val article: String? = null,
    private val topSort: String? = null
) : PagingSource<String, Content>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, Content> {
        val after = params.key
        return try {

            val listing = getListing(
                listingType = listingType,
                service = service,
                tokenHeader = tokenHeader,
                sort = sort,
                topSort = topSort,
                subreddit = subreddit,
                username = username,
                article = article,
                after = after,
            )

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

    private suspend fun getListing(
        listingType: PagingListing,
        service: RedditApiService,
        tokenHeader: String,
        sort: String,
        subreddit: String? = null,
        username: String? = null,
        after: String? = null,
        article: String? = null,
        topSort: String? = null
    ): Listing {
        return when (listingType) {
            PagingListing.POSTS -> {
                parsePostListing(
                    service.getSubredditListing(
                        tokenHeader = tokenHeader,
                        subreddit = subreddit!!,
                        sort = sort,
                        topSort = topSort,
                        after = after
                    )
                )
            }

            PagingListing.DUPLICATES -> {
                parsePostListing(
                    service.getDuplicates(
                        tokenHeader = tokenHeader,
                        subreddit = subreddit!!,
                        article = article!!,
                        sort = sort,
                        after = after
                    ).jsonArray[1]
                )
            }

            PagingListing.USERSUBMISSIONS -> {
                parsePostListing(
                    service.getUser(
                        tokenHeader = tokenHeader,
                        username = username!!,
                        data = UserListingType.SUBMITTED.value,
                        sort = sort,
                        topSort = topSort,
                        after = after
                    )
                )
            }

            PagingListing.USERCOMMENTS -> {
                parseProfileCommentListing(
                    service.getUser(
                        tokenHeader = tokenHeader,
                        username = username!!,
                        data = UserListingType.COMMENTS.value,
                        sort = sort,
                        topSort = topSort,
                        after = after
                    ).jsonObject
                )
            }
        }
    }
}