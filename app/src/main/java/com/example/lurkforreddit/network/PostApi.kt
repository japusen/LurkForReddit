package com.example.lurkforreddit.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostListing(
    val after: String? = null,
    val before: String? = null,
    val dist: Int? = null,
    val children: List<PostApi>
) {
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
data class PostApi(
    override val created: Float,
    @SerialName("created_utc")
    override val createdUtc: Float,
    override val ups: Int,
    override val downs: Int,
    val id: String,
    val author: String,
    val domain: String?,
    val is_self: Boolean,
    val locked: Boolean,
    @SerialName("num_comments")
    val numComments: Int,
    @SerialName("over_18")
    val over18: Boolean,
    val score: Int,
    val selftext: String,
    @SerialName("selftext_html")
    val selfTextHtml: String? = null,
    val subreddit: String,
    @SerialName("subreddit_id")
    val subredditID: String,
    val thumbnail: String,
    val title: String,
    val distinguished: String? = null
) : Created, Votable {
    override fun toString(): String {
        return "\nPost: $id"
    }
}

