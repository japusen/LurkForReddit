package com.example.lurkforreddit.ui.home

import com.example.lurkforreddit.domain.model.ListingSort
import com.example.lurkforreddit.domain.model.SearchResult
import com.example.lurkforreddit.domain.model.TopSort
import com.example.lurkforreddit.ui.subreddit.ListingNetworkResponse

data class HomeUiState(
    val networkResponse: ListingNetworkResponse = ListingNetworkResponse.Loading,
    val subreddit: String = "All",
    val listingSort: ListingSort = ListingSort.HOT,
    val topSort: TopSort? = null,
    val query: String = "",
    val searchResult: List<SearchResult> = listOf()
)