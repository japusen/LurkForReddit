package com.example.lurkforreddit.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


interface CommentThreadItem {
    val name: String
    val id: String
    val parentID: String
    val depth: Int
}

@Serializable
data class ProfileComment(
    override val id: String,
    override val author: String,
    override val distinguished: String? = null,
    override val score: Int,
    override val subreddit: String,
    @SerialName("subreddit_id")
    override val subredditID: String,
    override val created: Float,
    @SerialName("created_utc")
    override val createdUtc: Float,
    @SerialName("link_title")
    val linkTitle: String,
    @SerialName("link_id")
    val linkID: String,
    @SerialName("score_hidden")
    val scoreHidden: Boolean,
    val body: String,
    @SerialName("body_html")
    val bodyHtml: String,
) : Content, Created

@Serializable
data class Comment(
    override val name: String,
    override val id: String,
    @SerialName("parent_id")
    override val parentID: String,
    override val depth: Int,
    override val author: String,
    override val distinguished: String? = null,
    override val score: Int,
    override val subreddit: String,
    @SerialName("subreddit_id")
    override val subredditID: String,
    override val created: Float,
    @SerialName("created_utc")
    override val createdUtc: Float,
    @SerialName("score_hidden")
    val scoreHidden: Boolean,
    val body: String,
    @SerialName("body_html")
    val bodyHtml: String,
    @SerialName("link_id")
    val linkID: String,
) : Content, CommentThreadItem, Created
