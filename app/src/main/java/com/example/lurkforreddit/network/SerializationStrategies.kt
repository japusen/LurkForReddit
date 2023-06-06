package com.example.lurkforreddit.network

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
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
            PostApi.serializer(),
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
            ProfileCommentApi.serializer(),
            it.jsonObject.getOrDefault("data", blank)
        )
    }

    return ProfileCommentListing(after, before, dist, comments)
}


fun parseComments(
    response: JsonElement
): Pair<List<CommentContents>, MoreApi?> {
    val comments = mutableListOf<CommentContents>()
    var more: MoreApi? = null

    val data = response.jsonObject.getOrDefault("data", blank)
    val children = data.jsonObject.getOrDefault("children", blank) as JsonArray

    for (child in children) {
        val kind = json.decodeFromJsonElement(
            String.serializer(),
            child.jsonObject.getOrDefault("kind", blank)
        )
        if (kind == "more") {
            more = json.decodeFromJsonElement(
                MoreApi.serializer(),
                child.jsonObject.getOrDefault("data", blank)
            )
        } else {
            val commentData = child.jsonObject.getOrDefault("data", blank)
            val comment = json.decodeFromJsonElement(
                CommentContents.serializer(),
                commentData
            )
            comments.add(comment)
        }
    }

    return Pair(comments, more)
}