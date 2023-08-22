package com.example.lurkforreddit.data.remote.model

import com.example.lurkforreddit.domain.model.CommentThreadItem
import com.example.lurkforreddit.domain.model.Content
import com.example.lurkforreddit.domain.model.Created
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CommentDto(
    override var visible: Boolean = true,
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
    val permalink: String,
) : Content, CommentThreadItem, Created
