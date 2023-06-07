package com.example.lurkforreddit.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MoreApi(
    val name: String,
    val count: Int,
    val id: String,
    @SerialName("parent_id")
    val parentID: String,
    val children: List<String>
) {
    override fun toString(): String {
        return "More: ${children.size}"
    }
}
