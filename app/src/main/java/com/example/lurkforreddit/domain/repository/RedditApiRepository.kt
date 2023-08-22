package com.example.lurkforreddit.domain.repository

import androidx.paging.PagingData
import com.example.lurkforreddit.data.remote.model.PostDto
import com.example.lurkforreddit.data.remote.model.SearchResultDto
import com.example.lurkforreddit.domain.model.CommentSort
import com.example.lurkforreddit.domain.model.CommentThreadItem
import com.example.lurkforreddit.domain.model.Content
import com.example.lurkforreddit.domain.model.DuplicatesSort
import com.example.lurkforreddit.domain.model.ListingSort
import com.example.lurkforreddit.domain.model.TopSort
import com.example.lurkforreddit.domain.model.UserListingSort
import kotlinx.coroutines.flow.Flow

interface RedditApiRepository {

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

    suspend fun getCommentThread(
        subreddit: String,
        article: String,
        sort: CommentSort
    ): Pair<PostDto, MutableList<CommentThreadItem>>

    suspend fun subredditAutoComplete(
        query: String,
    ): List<SearchResultDto>

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