package com.example.lurkforreddit.data.remote.model

import com.example.lurkforreddit.domain.model.CommentThreadItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoreDto(
    override var visible: Boolean = true,
    override val depth: Int,
    val children: List<String>
): CommentThreadItem
