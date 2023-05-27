package com.example.lurkforreddit.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val LISTING = "Listing"

@Serializable
@SerialName(LISTING)
data class Listing(
    val after: String,
    val before: String,
    val dist: Int,
    val children: List<Thing>
) : Thing()
