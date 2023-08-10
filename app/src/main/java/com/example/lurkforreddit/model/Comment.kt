package com.example.lurkforreddit.model

import android.util.Log
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class Comment(
    val contents: CommentContents?,
    val replies: MutableList<Comment>,
    val more: More?
) {
    fun insert(parentID: String, comments: List<Comment>): Comment {
        if (contents?.id == parentID) {
            Log.d("MORE", "insert to ${this.contents.body}")
            return this.copy(
                replies = replies.apply {
                    addAll(comments)
                }
            )
        }

        replies.map { comment -> comment.insert(parentID, comments) }

        return this
    }
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
    @SerialName("score_hidden")
    val scoreHidden: Boolean,
    val body: String,
    @SerialName("body_html")
    val bodyHtml: String,
    @SerialName("link_id")
    val linkID: String,
    @SerialName("parent_id")
    val parentID: String,
) : Content, Created
