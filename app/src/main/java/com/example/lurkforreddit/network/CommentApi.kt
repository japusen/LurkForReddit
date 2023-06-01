package com.example.lurkforreddit.network

import com.example.lurkforreddit.model.Comment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonTransformingSerializer

private const val COMMENT = "t1"

@Serializable
@SerialName(COMMENT)
data class CommentApi(
    val data: CommentData
) : Thing()

@Serializable
data class CommentData(
    override val created: Float,
    @SerialName("created_utc")
    override val createdUtc: Float,
    override val ups: Int,
    override val downs: Int,
    val author: String,
    val body: String,
    @SerialName("body_html")
    val bodyHtml: String,
    @SerialName("link_id")
    val linkID: String,
    @Serializable(with = RepliesSerializer::class)
    val replies: List<Thing>,
    val score: Int,
    @SerialName("score_hidden")
    val scoreHidden: Boolean,
    val subreddit: String,
    @SerialName("subreddit_id")
    val subredditID: String,
    val distinguished: String?
) : Created, Votable {
    fun toComment() = Comment(
        created = created,
        createdUtc = createdUtc,
        ups = ups,
        downs = downs,
        author = author,
        body = body,
        bodyHtml = bodyHtml,
        linkID = linkID,
        replies = listOf(), /* TODO */
        score = score,
        scoreHidden = scoreHidden,
        subreddit = subreddit,
        subredditID = subredditID,
        distinguished = distinguished ?: ""
    )
}


object RepliesSerializer : JsonTransformingSerializer<List<Thing>>(ListSerializer(Thing.serializer())) {
    override fun transformDeserialize(element: JsonElement): JsonElement =
        /* TODO: Replies is a listing, currently always an empty array */
        if (element !is JsonArray) JsonArray(listOf()) else element
}