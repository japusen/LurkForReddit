package com.example.lurkforreddit.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileCommentListing(
    val after: String?,
    val before: String?,
    val dist: Int? = null,
    val children: List<ProfileCommentApi>
) {
    override fun toString(): String {
        return "ProfileCommentListing\n" +
                "----------\n" +
                "after = $after\n" +
                "before = $before\n" +
                "dist = $dist\n" +
                "children = $children\n"
    }
}

@Serializable
data class ProfileCommentApi(
    override val created: Float,
    @SerialName("created_utc")
    override val createdUtc: Float,
    override val ups: Int,
    override val downs: Int,
    val id: String,
    val author: String,
    val body: String,
    @SerialName("body_html")
    val bodyHtml: String,
    @SerialName("link_id")
    val linkID: String,
    val score: Int,
    @SerialName("score_hidden")
    val scoreHidden: Boolean,
    val subreddit: String,
    @SerialName("subreddit_id")
    val subredditID: String,
    val distinguished: String?
) : Created, Votable {
    override fun toString(): String {
        return "\nComment: $id"
    }
}