package com.example.lurkforreddit.ui.home

import androidx.paging.PagingData
import com.example.lurkforreddit.domain.model.ListingSort
import com.example.lurkforreddit.domain.model.Post
import com.example.lurkforreddit.domain.model.SearchResult
import com.example.lurkforreddit.domain.model.TopSort
import com.example.lurkforreddit.domain.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

data class HomeUiState(
    val networkResponse: NetworkResponse<Flow<PagingData<Post>>> = NetworkResponse.Loading,
    val subreddit: String = "All",
    val listingSort: ListingSort = ListingSort.HOT,
    val topSort: TopSort? = null,
    val query: String = "",
    val searchResult: List<SearchResult> = listOf()
)