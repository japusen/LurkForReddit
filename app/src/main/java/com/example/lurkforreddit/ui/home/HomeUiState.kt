package com.example.lurkforreddit.ui.home

import androidx.paging.PagingData
import com.example.lurkforreddit.domain.model.Content
import com.example.lurkforreddit.domain.model.ListingSort
import com.example.lurkforreddit.domain.model.NetworkResponse
import com.example.lurkforreddit.domain.model.SearchResult
import com.example.lurkforreddit.domain.model.TopSort
import kotlinx.coroutines.flow.Flow

data class HomeUiState(
    val networkResponse: NetworkResponse<Flow<PagingData<Content>>> = NetworkResponse.Loading,
    val subreddit: String = "All",
    val listingSort: ListingSort = ListingSort.HOT,
    val topSort: TopSort? = null,
    val query: String = "",
    val searchResult: List<SearchResult> = listOf()
)