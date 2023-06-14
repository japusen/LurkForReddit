package com.example.lurkforreddit.network.model

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
    @SerialName("link_title")
    val linkTitle: String,
    @SerialName("link_id")
    val linkID: String,
    @SerialName("score_hidden")
    val scoreHidden: Boolean,
    val body: String,
    @SerialName("body_html")
    val bodyHtml: String,
) : Content, Created, Votable {
    override fun toString(): String {
        return "\nComment: $id"
    }
}

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
) : Content, Created, Votable {
    override fun toString(): String {
        return "$body\n"
    }
}

data class CommentApi(
    val contents: CommentContents?,
    val replies: List<CommentApi>,
    val more: MoreApi?
) {
    override fun toString(): String {
        return "${contents?.body}\n" +
                "$replies\n" +
                "$more"
    }
}