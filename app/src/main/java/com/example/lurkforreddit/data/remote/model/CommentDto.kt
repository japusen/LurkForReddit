package com.example.lurkforreddit.data.remote.model

import com.example.lurkforreddit.domain.model.CommentThreadItem
import com.example.lurkforreddit.domain.model.Content
import com.example.lurkforreddit.domain.model.Created
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CommentDto(
    override val visible: Boolean = true,
    override val id: String,
    override val depth: Int,
    override val author: String,
    override val distinguished: String? = null,
    override val score: Int,
    override val subreddit: String,
    @SerialName("created_utc")
    override val createdUtc: Float,
    @SerialName("score_hidden")
    val scoreHidden: Boolean,
    val body: String,
    @SerialName("body_html")
    val bodyHtml: String,
    @SerialName("permalink")
    val permalink: String,
) : Content, CommentThreadItem, Created
