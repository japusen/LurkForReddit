package com.example.lurkforreddit.domain.repository

import androidx.paging.PagingData
import com.example.lurkforreddit.domain.model.ListingSort
import com.example.lurkforreddit.domain.model.Post
import com.example.lurkforreddit.domain.model.TopSort
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getPosts(
        subreddit: String,
        sort: ListingSort,
        topSort: TopSort? = null
    ): Flow<PagingData<Post>>

    fun getPostHistory(): Flow<List<Post>>

    suspend fun savePostToHistory(post: Post)

    suspend fun clearPostHistory()
}