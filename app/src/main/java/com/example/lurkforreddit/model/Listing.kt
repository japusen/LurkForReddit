package com.example.lurkforreddit.model

import kotlinx.serialization.Serializable

@Serializable
data class PostListing(
    override val after: String? = null,
    override val before: String? = null,
    override val dist: Int? = null,
    override val children: List<Post>
) : Listing

@Serializable
data class ProfileCommentListing(
    override val after: String?,
    override val before: String?,
    override val dist: Int? = null,
    override val children: List<ProfileComment>
) : Listing