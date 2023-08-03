package com.example.lurkforreddit.network

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
                Pair(mutableListOf(), null)
            }
            val commentContent = json.decodeFromJsonElement(
                CommentContents.serializer(),
                commentData
            )
            comments.add(Comment(commentContent, replies.first.toMutableList(), replies.second))
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

fun parseMoreComments(
    response: JsonElement,
    root: String
): List<Comment> {
    val comments = mutableListOf<Comment>()
    val replyTree = mutableMapOf<String, Comment>()

    val data = response.jsonObject.getOrDefault("json", blank).jsonObject.getOrDefault("data", blank)
    val things = data.jsonObject.getOrDefault("things", blank) as JsonArray

    /** Assemble each comment and add it to the map by it's full id */
    for (thing in things) {
        val kind = thing.jsonObject["kind"]?.jsonPrimitive?.content
        if (kind == "t1") {
            val commentData = thing.jsonObject.getOrDefault("data", blank)
            val commentContent = json.decodeFromJsonElement(
                CommentContents.serializer(),
                commentData
            )
            replyTree["t1_${commentContent.id}"] = Comment(commentContent, mutableListOf(), null)
        }
    }

    /** if a reply has the root as it's parent, add it to the list of comments
     otherwise find it's parent in the map and add it to the parent's replies **/
    for (reply in replyTree) {
        val comment = reply.value
        val parentID = comment.contents?.parentID
        if (parentID == root) {
            comments.add(comment)
        } else {
            replyTree[parentID]?.replies?.add(comment)
        }
    }

    return comments
}

fun parseVredditUrl(
    media: JsonElement
): String {
    return media.jsonObject["reddit_video"]?.jsonObject?.get("dash_url")?.jsonPrimitive?.content ?: ""
}