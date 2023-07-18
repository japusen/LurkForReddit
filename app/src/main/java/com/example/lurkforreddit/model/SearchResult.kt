package com.example.lurkforreddit.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    val name: String,
    val communityIcon: String,
    val icon: String,
)
