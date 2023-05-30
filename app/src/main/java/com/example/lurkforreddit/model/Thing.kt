package com.example.lurkforreddit.model

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = ModuleSerializer::class)
abstract class Thing

interface Votable {
    val ups: Int
    val downs: Int
}

interface Created {
    val created: Float
    val createdUtc: Float
}

object ModuleSerializer : JsonContentPolymorphicSerializer<Thing>(Thing::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Thing> {
        return when (element.jsonObject["kind"]?.jsonPrimitive?.content) {
            "Listing" -> Listing.serializer()
            "t3" -> Link.serializer()
            "t1" -> Comment.serializer()
            "more" -> MoreComments.serializer()
            else -> throw Exception("Unknown Module: key 'kind' not found or does not matches any module type")
        }
    }
}