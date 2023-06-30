package com.example.lurkforreddit.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


@Serializable
data class Post(
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
    val is_self: Boolean,
    val is_gallery: Boolean = false,
    @SerialName("post_hint")
    val postType: String? = null,
    val thumbnail: String,
    val preview: JsonElement? = null,
    val title: String,
    val selftext: String,
    @SerialName("selftext_html")
    val selfTextHtml: String? = null,
    @SerialName("num_comments")
    val numComments: Int,
    val domain: String?,
    val url: String,
    val locked: Boolean,
    @SerialName("over_18")
    val over18: Boolean,
    val media: JsonElement? = null
) : Content, Created, Votable

