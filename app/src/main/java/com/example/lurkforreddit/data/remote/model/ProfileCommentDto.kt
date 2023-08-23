package com.example.lurkforreddit.data.remote.model

import com.example.lurkforreddit.domain.model.Content
import com.example.lurkforreddit.domain.model.Created
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ProfileCommentDto(
    override val id: String,
    override val author: String,
    override val distinguished: String? = null,
    override val score: Int,
    override val subreddit: String,
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