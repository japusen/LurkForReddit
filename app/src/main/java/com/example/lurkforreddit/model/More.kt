package com.example.lurkforreddit.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class More(
    val name: String,
    val count: Int,
    val id: String,
    @SerialName("parent_id")
    val parentID: String,
    var children: List<String>
) {
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
