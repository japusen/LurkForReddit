package com.example.lurkforreddit.domain.repository

import androidx.paging.PagingData
import com.example.lurkforreddit.domain.model.Content
import com.example.lurkforreddit.domain.model.TopSort
import com.example.lurkforreddit.domain.model.UserListingSort
import kotlinx.coroutines.flow.Flow

interface ProfileContentRepository {
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
}