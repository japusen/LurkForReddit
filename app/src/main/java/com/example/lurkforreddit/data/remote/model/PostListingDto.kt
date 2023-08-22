package com.example.lurkforreddit.data.remote.model

import com.example.lurkforreddit.domain.model.Listing
import kotlinx.serialization.Serializable

@Serializable
data class PostListingDto(
    override val after: String? = null,
    override val before: String? = null,
    override val dist: Int? = null,
    override val children: List<PostDto>
) : Listing
