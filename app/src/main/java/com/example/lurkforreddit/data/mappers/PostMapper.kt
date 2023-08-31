package com.example.lurkforreddit.data.mappers

import com.example.lurkforreddit.data.local.PostEntity
import com.example.lurkforreddit.data.remote.model.PostDto
import com.example.lurkforreddit.domain.model.Post
import com.example.lurkforreddit.util.relativeTime

fun PostDto.toPost(): Post {
    return Post(
        id = id,
        author = author,
        distinguished = distinguished,
        score = score,
        subreddit = subreddit,
        time = relativeTime(createdUtc),
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

fun Post.toPostEntity(): PostEntity {
    return PostEntity(
        postId = id,
        author = author,
        distinguished = distinguished,
        score = score,
        subreddit = subreddit,
        time = time,
        isSelfPost = isSelfPost,
        isGalleryPost = isGalleryPost,
        thumbnail = thumbnail,
        title = title,
        selftext = selftext,
        selfTextHtml = selfTextHtml,
        numComments = numComments,
        domain = domain,
        url = url,
        locked = locked,
        nsfw = nsfw,
    )
}

fun PostEntity.toPost(): Post {
    return Post(
        id = postId,
        author = author,
        distinguished = distinguished,
        score = score,
        subreddit = subreddit,
        time = time,
        isSelfPost = isSelfPost,
        isGalleryPost = isGalleryPost,
        thumbnail = thumbnail,
        title = title,
        selftext = selftext,
        selfTextHtml = selfTextHtml,
        numComments = numComments,
        domain = domain,
        url = url,
        locked = locked,
        nsfw = nsfw,
    )
}