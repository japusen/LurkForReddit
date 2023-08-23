package com.example.lurkforreddit.domain.model

data class Comment(
    override val visible: Boolean,
    override val id: String,
    override val depth: Int,
    override val author: String,
    override val subreddit: String,
    override val score: Int,
    val time: String,
    val text: String,
    val permalink: String,
    val isScoreHidden: Boolean,
    override val distinguished: String?
): Content, CommentThreadItem