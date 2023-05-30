package com.example.lurkforreddit.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val LISTING = "Listing"

enum class ListingSort(val type: String) {
    HOT("hot"),
    RISING("rising"),
    NEW("new")
}

enum class TopSort(val type: String) {
    HOUR("hour"),
    DAY("day"),
    WEEK("week"),
    MONTH("month"),
    YEAR("year"),
    ALL("all")
}

@Serializable
@SerialName(LISTING)
data class Listing(
    val data: ListingData
) : Thing()

@Serializable
data class ListingData(
    val after: String?,
    val before: String?,
    val dist: Int,
    val children: List<Thing>
)
