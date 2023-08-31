package com.example.lurkforreddit.ui.subreddit

import androidx.paging.PagingData
import com.example.lurkforreddit.domain.model.ListingSort
import com.example.lurkforreddit.domain.model.Post
import com.example.lurkforreddit.domain.model.TopSort
import com.example.lurkforreddit.domain.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

data class SubredditUiState(
    val networkResponse: NetworkResponse<Flow<PagingData<Post>>> = NetworkResponse.Loading,
    val listingSort: ListingSort = ListingSort.HOT,
    val topSort: TopSort? = null,
)