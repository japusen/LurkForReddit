package com.example.lurkforreddit.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val LINK = "t3"

@Serializable
@SerialName(LINK)
data class Link(
    override val created: Long,
    @SerialName("created_utc")
    override val createdUtc: Long,
    override val ups: Int,
    override val downs: Int,
    val author: String,
    val domain: String,
    val is_self: Boolean,
    val locked: Boolean,
    @SerialName("num_comments")
    val numComments: Int,
    @SerialName("over_18")
    val over18: Boolean,
    val score: Int,
    val selftext: String,
    @SerialName("selftext_html")
    val selfTextHtml: String,
    val subreddit: String,
    @SerialName("subreddit_id")
    val subredditID: String,
    val thumbnail: String,
    val title: String,
    val distinguished: String?
) : Thing(), Created, Votable