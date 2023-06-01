package com.example.lurkforreddit.network

import com.example.lurkforreddit.model.More
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("more")
data class MoreComments(
    val data: MoreCommentData
) : Thing() {
    fun toMore() = More(
        name = data.name,
        count = data.count,
        id = data.id,
        parentID = data.parentID,
        commentIDs = data.children
    )
}

@Serializable
data class MoreCommentData(
    val name: String,
    val count: Int,
    val id: String,
    @SerialName("parent_id")
    val parentID: String,
    val children: List<String>
)
