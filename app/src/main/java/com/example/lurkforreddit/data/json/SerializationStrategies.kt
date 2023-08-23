package com.example.lurkforreddit.data.json

import com.example.lurkforreddit.data.mappers.toComment
import com.example.lurkforreddit.data.mappers.toMore
import com.example.lurkforreddit.data.mappers.toPost
import com.example.lurkforreddit.data.mappers.toProfileComment
import com.example.lurkforreddit.data.remote.model.CommentDto
import com.example.lurkforreddit.data.remote.model.ProfileCommentDto
import com.example.lurkforreddit.domain.model.ProfileCommentListing
import com.example.lurkforreddit.domain.model.CommentThreadItem
import com.example.lurkforreddit.data.remote.model.MoreDto
import com.example.lurkforreddit.data.remote.model.PostDto
import com.example.lurkforreddit.data.remote.model.SearchResultDto
import com.example.lurkforreddit.domain.model.PostListing
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
    val children = listing?.jsonObject?.get("children")

    val posts = children?.jsonArray?.map {
        val postDto = json.decodeFromJsonElement(
            PostDto.serializer(),
            it.jsonObject.getOrDefault("data", empty)
        )
        postDto.toPost()
    }

    return PostListing(after, posts ?: listOf())
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
    val children = listing?.jsonObject?.get("children")

    val comments = children?.jsonArray?.map {
        val profileCommentDto = json.decodeFromJsonElement(
            ProfileCommentDto.serializer(),
            it.jsonObject.getOrDefault("data", empty)
        )
        profileCommentDto.toProfileComment()
    }

    return ProfileCommentListing(after, comments ?: listOf())
}

/**
 * Recursively parse the JSON response to obtain a list of CommentThreadItems
 * @param response the JSON Element
 * @return a MutableList of CommentThreadItems
 */
fun parsePostComments(
    response: JsonElement,
    commentThread: MutableList<CommentThreadItem>,
) {
    val data = response.jsonObject["data"]
    val children = data?.jsonObject?.getOrDefault("children", "[]") as JsonArray

    for (child in children) {
        val kind = json.decodeFromJsonElement(
            String.serializer(),
            child.jsonObject.getOrDefault("kind", empty)
        )
        if (kind == "more") {
            val moreDto = json.decodeFromJsonElement(
                MoreDto.serializer(),
                child.jsonObject.getOrDefault("data", empty)
            )
            val more = moreDto.toMore()
            commentThread.add(more)
        } else {
            val commentData = child.jsonObject.getOrDefault("data", empty)
            val commentDto = json.decodeFromJsonElement(
                CommentDto.serializer(),
                commentData
            )
            val comment = commentDto.toComment()
            commentThread.add(comment)

            val replies = commentData.jsonObject.getOrDefault("replies", empty)
            if (replies is JsonObject)
                parsePostComments(replies, commentThread)
        }
    }
}

/**
 * Recursively parse the JSON response to obtain a list of CommentThreadItems
 * @param response the JSON Element
 * @return a MutableList of CommentThreadItems
 */
fun parseMoreComments(
    response: JsonElement,
    commentThread: MutableList<CommentThreadItem>,
) {
    val data = response.jsonObject["json"]?.jsonObject?.get("data")
    val things = data?.jsonObject?.getOrDefault("things", "[]") as JsonArray

    for (thing in things) {
        val kind = json.decodeFromJsonElement(
            String.serializer(),
            thing.jsonObject.getOrDefault("kind", empty)
        )
        if (kind == "more") {
            val moreDto = json.decodeFromJsonElement(
                MoreDto.serializer(),
                thing.jsonObject.getOrDefault("data", empty)
            )
            val more = moreDto.toMore()
            commentThread.add(more)
        } else {
            val commentData = thing.jsonObject.getOrDefault("data", empty)
            val commentDto = json.decodeFromJsonElement(
                CommentDto.serializer(),
                commentData
            )
            val comment = commentDto.toComment()
            commentThread.add(comment)
        }
    }
}

/**
 * Parse the JSON response to obtain a list of search results
 * @param response the JSON response
 * @return a list of SearchResults or empty list
 */
fun parseSearchResults(
    response: JsonElement
): List<SearchResultDto> {
    return response.jsonObject["subreddits"]?.jsonArray?.map {
        json.decodeFromJsonElement(
            SearchResultDto.serializer(),
            it
        )
    } ?: listOf()
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