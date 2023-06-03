package com.example.lurkforreddit.network

import kotlinx.serialization.Serializable

@Serializable
data class PostListing(
    override val after: String? = null,
    override val before: String? = null,
    override val dist: Int? = null,
    override val children: List<PostApi>
) : Listing {
    override fun toString(): String {
        return "PostListing\n" +
                "----------\n" +
                "after = $after\n" +
                "before = $before\n" +
                "dist = $dist\n" +
                "children = $children\n"
    }
}

@Serializable
data class ProfileCommentListing(
    override val after: String?,
    override val before: String?,
    override val dist: Int? = null,
    override val children: List<ProfileCommentApi>
) : Listing {
    override fun toString(): String {
        return "ProfileCommentListing\n" +
                "----------\n" +
                "after = $after\n" +
                "before = $before\n" +
                "dist = $dist\n" +
                "children = $children\n"
    }
}