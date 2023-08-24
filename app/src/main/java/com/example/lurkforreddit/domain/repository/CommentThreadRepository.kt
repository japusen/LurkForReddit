package com.example.lurkforreddit.domain.repository

import com.example.lurkforreddit.domain.model.CommentSort
import com.example.lurkforreddit.domain.model.CommentThreadItem
import com.example.lurkforreddit.domain.model.Post

interface CommentThreadRepository {
    suspend fun getCommentThread(
        subreddit: String,
        article: String,
        sort: CommentSort
    ): Pair<Post, List<CommentThreadItem>>

    suspend fun addComments(
        index: Int,
        linkID: String,
        sort: CommentSort,
    ): List<CommentThreadItem>

    suspend fun changeCommentVisibility(
        show: Boolean,
        index: Int,
        depth: Int
    ): List<CommentThreadItem>
}