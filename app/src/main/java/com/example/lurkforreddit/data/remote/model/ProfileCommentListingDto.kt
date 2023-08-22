package com.example.lurkforreddit.data.remote.model

import com.example.lurkforreddit.domain.model.Listing
import kotlinx.serialization.Serializable

@Serializable
data class ProfileCommentListingDto(
    override val after: String?,
    override val before: String?,
    override val dist: Int? = null,
    override val children: List<ProfileCommentDto>
) : Listing