package com.example.lurkforreddit.model

import com.example.lurkforreddit.network.ListingApi


data class SubredditFeed(
    val after: String,
    val before: String,
    val dist: Int,
    val posts: List<Post>
)

data class CommentTree(
    val after: String,
    val before: String,
    val dist: Int,
    val comments: List<Comment>,
    val moreComments: More
)


