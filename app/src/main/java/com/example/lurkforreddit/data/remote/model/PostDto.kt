package com.example.lurkforreddit.data.remote.model

import com.example.lurkforreddit.data.json.parseVredditUrl
import com.example.lurkforreddit.domain.model.Content
import com.example.lurkforreddit.domain.model.Created
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@Serializable
data class PostDto(
    override val id: String,
    override val author: String,
    override val distinguished: String? = null,
    override val score: Int,
    override val subreddit: String,
    @SerialName("created_utc")
    override val createdUtc: Float,
    @SerialName("is_self")
    val isSelfPost: Boolean,
    @SerialName("is_gallery")
    val isGalleryPost: Boolean = false,
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
) : Content, Created {

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
            parseVredditUrl(media).ifEmpty { url }
        else
            url
    }
}

