package com.example.lurkforreddit.domain.repository

import com.example.lurkforreddit.data.remote.model.PostDto
import com.example.lurkforreddit.domain.model.CommentSort
import com.example.lurkforreddit.domain.model.CommentThreadItem

interface CommentThreadRepository {
    suspend fun getCommentThread(
        subreddit: String,
        article: String,
        sort: CommentSort
    ): Pair<PostDto, MutableList<CommentThreadItem>>

    suspend fun getMoreComments(
        linkID: String,
        parentID: String,
        childrenIDs: String,
        sort: CommentSort,
    ): MutableList<CommentThreadItem>
}