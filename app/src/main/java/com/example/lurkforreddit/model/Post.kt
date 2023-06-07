package com.example.lurkforreddit.model

import com.example.lurkforreddit.network.model.Created
import com.example.lurkforreddit.network.model.Votable

data class Post(
    override val created: Float,
    override val createdUtc: Float,
    override val ups: Int,
    override val downs: Int,
    val author: String,
    val domain: String,
    val is_self: Boolean,
    val locked: Boolean,
    val numComments: Int,
    val over18: Boolean,
    val score: Int,
    val selftext: String,
    val selfTextHtml: String,
    val subreddit: String,
    val subredditID: String,
    val thumbnail: String,
    val title: String,
    val distinguished: String
): Created, Votable
