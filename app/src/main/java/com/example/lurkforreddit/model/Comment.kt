package com.example.lurkforreddit.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class Comment(
    val contents: CommentContents?,
    val replies: MutableList<Comment>,
    val more: More?
)

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
    override val ups: Int,
    override val downs: Int,
    @SerialName("link_title")
    val linkTitle: String,
    @SerialName("link_id")
    val linkID: String,
    @SerialName("score_hidden")
    val scoreHidden: Boolean,
    val body: String,
    @SerialName("body_html")
    val bodyHtml: String,
) : Content, Created, Votable

@Serializable
data class CommentContents(
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
    override val ups: Int,
    override val downs: Int,
    @SerialName("score_hidden")
    val scoreHidden: Boolean,
    val body: String,
    @SerialName("body_html")
    val bodyHtml: String,
    @SerialName("link_id")
    val linkID: String,
    @SerialName("parent_id")
    val parentID: String,
) : Content, Created, Votable
