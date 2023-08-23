package com.example.lurkforreddit.domain.model

data class PostListing(
    override val after: String? = null,
    override val children: List<Post>
) : Listing
