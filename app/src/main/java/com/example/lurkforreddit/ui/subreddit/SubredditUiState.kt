package com.example.lurkforreddit.ui.subreddit

import com.example.lurkforreddit.domain.model.ListingSort
import com.example.lurkforreddit.domain.model.TopSort

data class SubredditUiState(
    val networkResponse: ListingNetworkResponse = ListingNetworkResponse.Loading,
    val listingSort: ListingSort = ListingSort.HOT,
    val topSort: TopSort? = null,
)