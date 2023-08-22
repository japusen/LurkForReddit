package com.example.lurkforreddit.data.remote.model

import com.example.lurkforreddit.domain.model.CommentThreadItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MoreDto(
    override var visible: Boolean = true,
    override val name: String,
    override val id: String,
    @SerialName("parent_id")
    override val parentID: String,
    override val depth: Int,
    var children: List<String>
): CommentThreadItem {
    fun getIDs(count: Int): String {
        val ids: String
        if (children.size > count) {
            ids = children.subList(0, count).joinToString(",")
            children = children.subList(count, children.size)
        } else {
            ids = children.joinToString(",")
            children = listOf()
        }
        return ids
    }
}
