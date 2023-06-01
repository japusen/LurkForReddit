package com.example.lurkforreddit.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val LISTING = "Listing"

@Serializable
@SerialName(LISTING)
data class ListingApi(
    val data: ListingData
) : Thing()

@Serializable
data class ListingData(
    val after: String?,
    val before: String?,
    val dist: Int? = null,
    val children: List<Thing>
)