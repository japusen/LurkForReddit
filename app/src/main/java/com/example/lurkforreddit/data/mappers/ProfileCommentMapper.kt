package com.example.lurkforreddit.data.mappers

import com.example.lurkforreddit.data.remote.model.ProfileCommentDto
import com.example.lurkforreddit.domain.model.ProfileComment
import com.example.lurkforreddit.util.relativeTimeString

fun ProfileCommentDto.toProfileComment(): ProfileComment {
    return ProfileComment(
        id = id,
        author = author,
        distinguished = distinguished,
        score = score,
        subreddit = subreddit,
        time = relativeTimeString(createdUtc),
        postTitle = linkTitle,
        linkID = linkID,
        body = body,
        bodyHtml = bodyHtml
    )
}