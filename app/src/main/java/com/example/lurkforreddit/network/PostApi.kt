package com.example.lurkforreddit.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostApi(
    override val created: Float,
    @SerialName("created_utc")
    override val createdUtc: Float,
    override val ups: Int,
    override val downs: Int,
    val author: String,
    val domain: String?,
    val is_self: Boolean,
    val locked: Boolean,
    @SerialName("num_comments")
    val numComments: Int,
    @SerialName("over_18")
    val over18: Boolean,
    val score: Int,
    val selftext: String,
    @SerialName("selftext_html")
    val selfTextHtml: String?,
    val subreddit: String,
    @SerialName("subreddit_id")
    val subredditID: String,
    val thumbnail: String,
    val title: String,
    val distinguished: String?
) : Created, Votable