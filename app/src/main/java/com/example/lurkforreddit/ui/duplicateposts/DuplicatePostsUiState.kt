package com.example.lurkforreddit.ui.duplicateposts

import com.example.lurkforreddit.domain.model.DuplicatesSort
import com.example.lurkforreddit.ui.subreddit.ListingNetworkResponse

data class DuplicatesUiState(
    val networkResponse: ListingNetworkResponse = ListingNetworkResponse.Loading,
    val sort: DuplicatesSort = DuplicatesSort.NUMCOMMENTS,
)