package com.example.lurkforreddit.network

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
)
