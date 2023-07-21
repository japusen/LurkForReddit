package com.example.lurkforreddit.model

import android.util.Log
import com.example.lurkforreddit.network.parseVredditUrl
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@Serializable
data class Post(
    override val id: String,
    override val author: String,
    override val distinguished: String? = null,
    override val score: Int,
    override val subreddit: String,
    @SerialName("subreddit_id")
    override val subredditID: String,
    override val created: Float,
    @SerialName("created_utc")
    override val createdUtc: Float,
    override val ups: Int,
    override val downs: Int,
    val is_self: Boolean,
    val is_gallery: Boolean = false,
    @SerialName("post_hint")
    val postType: String? = null,
    val thumbnail: String,
    val preview: JsonElement? = null,
    val title: String,
    val selftext: String,
    @SerialName("selftext_html")
    val selfTextHtml: String? = null,
    @SerialName("num_comments")
    val numComments: Int,
    val domain: String?,
    val url: String,
    val locked: Boolean,
    @SerialName("over_18")
    val over18: Boolean,
    val media: JsonElement? = null
) : Content, Created, Votable {

    fun parseThumbnail(): String {
        return if (preview != null)
            preview.jsonObject["images"]?.jsonArray?.get(0)?.jsonObject?.get("source")?.jsonObject?.get("url")?.jsonPrimitive?.content ?: "none"
        else if ((thumbnail != "image") && (thumbnail != "default"))
            thumbnail
        else
            "none"
    }

    fun parseUrl(): String {
        return if (domain == "v.redd.it" && media != null)
            parseVredditUrl(media)
        else
            url
    }
}

