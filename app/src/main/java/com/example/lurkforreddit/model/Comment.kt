package com.example.lurkforreddit.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val COMMENT = "t1"

@Serializable
@SerialName(COMMENT)
data class Comment(
    override val created: Long,
    @SerialName("created_utc")
    override val createdUtc: Long,
    override val ups: Int,
    override val downs: Int,
    val author: String,
    val body: String,
    @SerialName("body_html")
    val bodyHtml: String,
    @SerialName("link_id")
    val linkID: String,
    val replies: List<Thing>,
    val score: Int,
    @SerialName("score_hidden")
    val scoreHidden: Boolean,
    val subreddit: String,
    @SerialName("subreddit_id")
    val subredditID: String,
    val distinguished: String?
) : Thing(), Created, Votable