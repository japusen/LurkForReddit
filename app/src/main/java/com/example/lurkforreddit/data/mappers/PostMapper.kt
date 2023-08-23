package com.example.lurkforreddit.data.mappers

import com.example.lurkforreddit.data.remote.model.PostDto
import com.example.lurkforreddit.domain.model.Post
import com.example.lurkforreddit.util.relativeTimeString

fun PostDto.toPost(): Post {
    return Post(
        id = id,
        author = author,
        distinguished = distinguished,
        score = score,
        subreddit = subreddit,
        time = relativeTimeString(createdUtc),
        isSelfPost = isSelfPost,
        isGalleryPost = isGalleryPost,
        thumbnail = parseThumbnail(),
        title = title,
        selftext = selftext,
        selfTextHtml = selfTextHtml,
        numComments = numComments,
        domain = domain,
        url = parseUrl(),
        locked = locked,
        nsfw = over18,
    )
}
