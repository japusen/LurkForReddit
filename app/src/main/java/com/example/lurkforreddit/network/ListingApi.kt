package com.example.lurkforreddit.network

import kotlinx.serialization.Serializable


@Serializable
data class PostListing(
    val after: String?,
    val before: String?,
    val dist: Int? = null,
    val children: List<PostApi>
)

@Serializable
data class ProfileCommentListing(
    val after: String?,
    val before: String?,
    val dist: Int? = null,
    val children: List<ProfileCommentApi>
)