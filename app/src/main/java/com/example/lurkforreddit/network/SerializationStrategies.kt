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

private val empty = json.parseToJsonElement("{}")

/**
 * Parse the JSON response to obtain a post listing
 * @param response the JSON response
 * @return a post listing
 */
fun parsePostListing(
    response: JsonElement
): PostListing {

    val listing = response.jsonObject["data"]

    val after = listing?.jsonObject?.get("after")?.jsonPrimitive?.contentOrNull
    val before = listing?.jsonObject?.get("before")?.jsonPrimitive?.contentOrNull
    val dist = listing?.jsonObject?.get("dist")?.jsonPrimitive?.content?.toInt()

    val children = listing?.jsonObject?.get("children")

    val posts = children?.jsonArray?.map {
        json.decodeFromJsonElement(
            Post.serializer(),
            it.jsonObject.getOrDefault("data", empty)
        )
    }

    return PostListing(after, before, dist, posts ?: listOf())
}

/**
 * Parse the JSON response to obtain a profile comment listing
 * @param response the JSON response
 * @return a profile comment listing
 */
fun parseProfileCommentListing(
    response: JsonElement
): ProfileCommentListing {

    val listing = response.jsonObject["data"]

    val after = listing?.jsonObject?.get("after")?.jsonPrimitive?.contentOrNull
    val before = listing?.jsonObject?.get("before")?.jsonPrimitive?.contentOrNull
    val dist = listing?.jsonObject?.get("dist")?.jsonPrimitive?.content?.toInt()

    val children = listing?.jsonObject?.get("children")

    val comments = children?.jsonArray?.map {
        json.decodeFromJsonElement(
            ProfileComment.serializer(),
            it.jsonObject.getOrDefault("data", empty)
        )
    }

    return ProfileCommentListing(after, before, dist, comments ?: listOf())
}

/**
 * Parse the JSON response to obtain a list of comments
 * @param response the JSON response
 * @return a list of Comments + More object
 */
fun parsePostComments(
    response: JsonElement
): Pair<List<Comment>, More?> {
    val comments = mutableListOf<Comment>()
    var more: More? = null

    val data = response.jsonObject["data"]
    val children = data?.jsonObject?.get("children") as JsonArray

    for (child in children) {
        val kind = json.decodeFromJsonElement(
            String.serializer(),
            child.jsonObject.getOrDefault("kind", empty)
        )
        if (kind == "more") {
            more = json.decodeFromJsonElement(
                More.serializer(),
                child.jsonObject.getOrDefault("data", empty)
            )
        } else {
            val commentData = child.jsonObject.getOrDefault("data", empty)
            val repliesData = commentData.jsonObject.getOrDefault("replies", empty)
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

/**
 * Parse the JSON response to obtain a list of search results
 * @param response the JSON response
 * @return a list of SearchResults or empty list
 */
fun parseSearchResults(
    response: JsonElement
): List<SearchResult> {
    return response.jsonObject["subreddits"]?.jsonArray?.map {
        json.decodeFromJsonElement(
            SearchResult.serializer(),
            it
        )
    } ?: listOf()
}

/**
 * Parse the JSON response to obtain a list of comments
 * @param response the JSON response
 * @param parentID the id of the post the comments are in response to
 * @return a list of comments
 */
fun parseMoreComments(
    response: JsonElement,
    parentID: String
): List<Comment> {
    val comments = mutableListOf<Comment>()
    val replyTree = mutableMapOf<String, Comment>()

    val data = response.jsonObject["json"]?.jsonObject?.get("data")
    val things = data?.jsonObject?.get("things") as JsonArray

    /** Assemble each comment and add it to the map by it's full id */
    for (thing in things) {
        val kind = thing.jsonObject["kind"]?.jsonPrimitive?.content
        if (kind == "t1") {
            val commentData = thing.jsonObject.getOrDefault("data", empty)
            val commentContent = json.decodeFromJsonElement(
                CommentContents.serializer(),
                commentData
            )
            replyTree["t1_${commentContent.id}"] = Comment(commentContent, mutableListOf(), null)
        }
    }

    Log.d("MORE", "reply tree: $replyTree")
    /** if a reply has the root as it's parent, add it to the list of comments
     otherwise find it's parent in the map and add it to the parent's replies **/
    for (reply in replyTree) {
        val comment = reply.value
        val replyParentID = comment.contents?.parentID
        if (replyParentID == parentID) {
            comments.add(comment)
        } else {
            replyTree[replyParentID]?.replies?.add(comment)
        }
    }

    return comments
}

/**
 * Parse the JSONElement to obtain the DASH url for the reddit video
 * @param media the JSONElement that holds the data
 * @return the DASH url of the video or an empty string
 */
fun parseVredditUrl(
    media: JsonElement
): String {
    return media.jsonObject["reddit_video"]?.jsonObject?.get("dash_url")?.jsonPrimitive?.content ?: ""
}