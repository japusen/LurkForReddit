package com.example.lurkforreddit.network

import com.example.lurkforreddit.model.Post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val LINK = "t3"

@Serializable
@SerialName(LINK)
data class LinkApi(
    val data: LinkData
) : Thing() {
    fun toPost() = Post(
        created = data.created,
        createdUtc = data.createdUtc,
        ups = data.ups,
        downs = data.downs,
        author = data.author,
        domain = data.domain ?: "",
        is_self = data.is_self,
        locked = data.locked,
        numComments = data.numComments,
        over18 = data.over18,
        score = data.score,
        selftext = data.selftext,
        selfTextHtml = data.selfTextHtml ?: "",
        subreddit = data.subreddit,
        subredditID = data.subredditID,
        thumbnail = data.thumbnail,
        title = data.title,
        distinguished = data.distinguished ?: ""
    )
}

@Serializable
data class LinkData(
    override val created: Float,
    @SerialName("created_utc")
    override val createdUtc: Float,
    override val ups: Int,
    override val downs: Int,
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
    val selfTextHtml: String?,
    val subreddit: String,
    @SerialName("subreddit_id")
    val subredditID: String,
    val thumbnail: String,
    val title: String,
    val distinguished: String?
) : Created, Votable