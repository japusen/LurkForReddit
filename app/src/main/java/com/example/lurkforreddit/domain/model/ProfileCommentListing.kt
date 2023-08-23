package com.example.lurkforreddit.domain.model

data class ProfileCommentListing(
    override val after: String? = null,
    override val children: List<ProfileComment>
) : Listing