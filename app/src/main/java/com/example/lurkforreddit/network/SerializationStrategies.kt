package com.example.lurkforreddit.network

import android.util.Log
import com.example.lurkforreddit.model.Comment
import com.example.lurkforreddit.model.CommentContents
import com.example.lurkforreddit.model.More
import com.example.lurkforreddit.model.Post
import com.example.lurkforreddit.model.PostListing
import com.example.lurkforreddit.model.ProfileComment
import com.example.lurkforreddit.model.ProfileCommentListing
import com.example.lurkforreddit.model.SearchResult
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private val json = Json {
    ignoreUnknownKeys = true
}

private val blank = json.parseToJsonElement("{}")

fun parsePostListing(
    response: JsonElement
): PostListing {

    val listing = response.jsonObject.getOrDefault("data", blank)

    val after = listing.jsonObject["after"]?.jsonPrimitive?.contentOrNull
    val before = listing.jsonObject["before"]?.jsonPrimitive?.contentOrNull
    val dist = listing.jsonObject["dist"]?.jsonPrimitive?.content?.toInt()

    val children = listing.jsonObject.getOrDefault("children", blank)

    val posts = children.jsonArray.map {
        json.decodeFromJsonElement(
            Post.serializer(),
            it.jsonObject.getOrDefault("data", blank)
        )
    }

    return PostListing(after, before, dist, posts)
}

fun parseProfileCommentListing(
    response: JsonElement
): ProfileCommentListing {

    val listing = response.jsonObject.getOrDefault("data", blank)

    val after = listing.jsonObject["after"]?.jsonPrimitive?.contentOrNull
    val before = listing.jsonObject["before"]?.jsonPrimitive?.contentOrNull
    val dist = listing.jsonObject["dist"]?.jsonPrimitive?.content?.toInt()

    val children = listing.jsonObject.getOrDefault("children", blank)

    val comments = children.jsonArray.map {
        json.decodeFromJsonElement(
            ProfileComment.serializer(),
            it.jsonObject.getOrDefault("data", blank)
        )
    }

    return ProfileCommentListing(after, before, dist, comments)
}


fun parsePostComments(
    response: JsonElement
): Pair<List<Comment>, More?> {
    val comments = mutableListOf<Comment>()
    var more: More? = null

    val data = response.jsonObject.getOrDefault("data", blank)
    val children = data.jsonObject.getOrDefault("children", blank) as JsonArray

    for (child in children) {
        val kind = json.decodeFromJsonElement(
            String.serializer(),
            child.jsonObject.getOrDefault("kind", blank)
        )
        if (kind == "more") {
            more = json.decodeFromJsonElement(
                More.serializer(),
                child.jsonObject.getOrDefault("data", blank)
            )
        } else {
            val commentData = child.jsonObject.getOrDefault("data", blank)
            val repliesData = commentData.jsonObject.getOrDefault("replies", blank)
            val replies = if (repliesData is JsonObject) {
                parsePostComments(repliesData)
            } else {
                Pair(listOf(), null)
            }
            val commentContent = json.decodeFromJsonElement(
                CommentContents.serializer(),
                commentData
            )
            comments.add(Comment(commentContent, replies.first, replies.second))
        }
    }

    return Pair(comments, more)
}

fun parseSearchResults(
    response: JsonElement
): List<SearchResult>? {
    return response.jsonObject["subreddits"]?.jsonArray?.map {
        json.decodeFromJsonElement(
            SearchResult.serializer(),
            it
        )
    }
}

fun parseVredditUrl(
    media: JsonElement
): String {
    return media.jsonObject["reddit_video"]?.jsonObject?.get("fallback_url")?.jsonPrimitive?.content ?: ""
}