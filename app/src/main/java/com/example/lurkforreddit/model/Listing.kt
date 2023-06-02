package com.example.lurkforreddit.model

abstract class Listing(
    open val after: String,
    open val before: String,
    open val dist: Int,
)

data class SubredditFeed(
    override val after: String,
    override val before: String,
    override val dist: Int,
    val posts: List<Post>
): Listing(after, before, dist)

data class CommentTree(
    override val after: String,
    override val before: String,
    override val dist: Int,
    val comments: List<Comment>,
    val moreComments: More
): Listing(after, before, dist)


