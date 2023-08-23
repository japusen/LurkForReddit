package com.example.lurkforreddit.domain.model

data class ProfileComment(
    override val id: String,
    override val author: String,
    override val distinguished: String?,
    override val score: Int,
    override val subreddit: String,
    val time: String,
    val postTitle: String,
    val linkID: String,
    val body: String,
    val bodyHtml: String,
): Content