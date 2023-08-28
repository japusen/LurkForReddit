package com.example.lurkforreddit.ui.profile

import androidx.paging.PagingData
import com.example.lurkforreddit.domain.model.Content
import com.example.lurkforreddit.domain.util.NetworkResponse
import com.example.lurkforreddit.domain.model.TopSort
import com.example.lurkforreddit.domain.model.UserContentType
import com.example.lurkforreddit.domain.model.UserListingSort
import kotlinx.coroutines.flow.Flow

data class ProfileUiState(
    val networkResponse: NetworkResponse<Flow<PagingData<Content>>> = NetworkResponse.Loading,
    val contentType: UserContentType = UserContentType.SUBMITTED,
    val userListingSort: UserListingSort = UserListingSort.HOT,
    val topSort: TopSort? = null,
)