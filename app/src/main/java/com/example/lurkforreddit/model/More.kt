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
    val children: List<String>
)
