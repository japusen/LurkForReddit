package com.example.lurkforreddit.domain.model

data class Post(
    override val id: String,
    override val author: String,
    override val distinguished: String?,
    override val score: Int,
    override val subreddit: String,
    val time: String,
    val isSelfPost: Boolean,
    val isGalleryPost: Boolean,
    val thumbnail: String,
    val title: String,
    val selftext: String,
    val selfTextHtml: String?,
    val numComments: Int,
    val domain: String?,
    val url: String,
    val locked: Boolean,
    val nsfw: Boolean,
): Content