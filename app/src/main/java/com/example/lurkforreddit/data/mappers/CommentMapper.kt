package com.example.lurkforreddit.data.mappers

import com.example.lurkforreddit.data.remote.model.CommentDto
import com.example.lurkforreddit.domain.model.Comment
import com.example.lurkforreddit.util.relativeTimeString

fun CommentDto.toComment(): Comment {
    return Comment(
        visible = true, // TODO visibility based on depth
        id = id,
        depth = depth,
        author = author,
        subreddit = subreddit,
        score = score,
        time = relativeTimeString(createdUtc),
        text = body,
        permalink = permalink,
        isScoreHidden = scoreHidden,
        distinguished = distinguished
    )
}