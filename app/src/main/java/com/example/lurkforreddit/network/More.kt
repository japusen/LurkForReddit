package com.example.lurkforreddit.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("more")
data class MoreComments(
    val data: MoreCommentData
) : Thing()

@Serializable
data class MoreCommentData(
    val name: String,
    val count: Int,
    val id: String,
    @SerialName("parent_id")
    val parentID: String,
    val children: List<String>
)
