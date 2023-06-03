package com.example.lurkforreddit.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ProfileCommentApi(
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
) : Content, Created, Votable {
    override fun toString(): String {
        return "\nComment: $id"
    }
}

@Serializable
data class PostCommentsApi(
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
    val replies: List<PostCommentsApi>? = emptyList()
) : Content, Created, Votable {
    override fun toString(): String {
        return "\nComment: $id"
    }
}