package com.example.lurkforreddit.ui.comments

import com.example.lurkforreddit.domain.model.CommentSort

data class CommentsUiState(
    val networkResponse: CommentsNetworkResponse = CommentsNetworkResponse.Loading,
    val commentSort: CommentSort = CommentSort.BEST
)