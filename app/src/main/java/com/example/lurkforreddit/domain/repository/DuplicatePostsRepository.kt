package com.example.lurkforreddit.domain.repository

import androidx.paging.PagingData
import com.example.lurkforreddit.domain.model.DuplicatesSort
import com.example.lurkforreddit.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface DuplicatePostsRepository {
    suspend fun getDuplicatePosts(
        subreddit: String,
        article: String,
        sort: DuplicatesSort
    ): Flow<PagingData<Post>>
}