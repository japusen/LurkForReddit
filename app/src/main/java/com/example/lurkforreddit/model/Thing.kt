package com.example.lurkforreddit.model

import android.util.Log
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = ModuleSerializer::class)
abstract class Thing


interface Votable {
    val ups: Int
    val downs: Int
}

interface Created {
    val created: Long
    val createdUtc: Long
}

object RedditThingDeserializer :
    JsonTransformingSerializer<Thing>(PolymorphicSerializer(Thing::class)) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        val kind = element.jsonObject["kind"]!!
        val data = element.jsonObject["data"] ?: return element
        Log.d("DESERIALIZE", data.toString())
        return JsonObject(data.jsonObject.toMutableMap().also { it["kind"] = kind })
    }
}

object ModuleSerializer : JsonContentPolymorphicSerializer<Thing>(Thing::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Thing> {
        return when (element.jsonObject["kind"]?.jsonPrimitive?.content) {
            "Listing" -> Listing.serializer()
            "t3" -> Link.serializer()
            "t1" -> Comment.serializer()
            else -> throw Exception("Unknown Module: key 'kind' not found or does not matches any module type")
        }
    }
}