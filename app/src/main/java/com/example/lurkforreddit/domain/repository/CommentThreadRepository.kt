package com.example.lurkforreddit.domain.repository

import com.example.lurkforreddit.domain.model.CommentSort
import com.example.lurkforreddit.domain.model.CommentThreadItem
import com.example.lurkforreddit.domain.model.Post

interface CommentThreadRepository {
    suspend fun getCommentThread(
        subreddit: String,
        article: String,
        sort: CommentSort
    ): Pair<Post, MutableList<CommentThreadItem>>

    suspend fun addComments(
        index: Int,
        linkID: String,
        ids: String,
        sort: CommentSort,
    ): MutableList<CommentThreadItem>
}