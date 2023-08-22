package com.example.lurkforreddit.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchResultDto(
    val name: String,
    val communityIcon: String,
    val icon: String,
)
