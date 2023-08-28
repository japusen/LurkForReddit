package com.example.lurkforreddit.ui.profile

import com.example.lurkforreddit.domain.model.TopSort
import com.example.lurkforreddit.domain.model.UserContentType
import com.example.lurkforreddit.domain.model.UserListingSort
import com.example.lurkforreddit.ui.subreddit.ListingNetworkResponse

data class ProfileUiState(
    val networkResponse: ListingNetworkResponse = ListingNetworkResponse.Loading,
    val contentType: UserContentType = UserContentType.SUBMITTED,
    val userListingSort: UserListingSort = UserListingSort.HOT,
    val topSort: TopSort? = null,
)