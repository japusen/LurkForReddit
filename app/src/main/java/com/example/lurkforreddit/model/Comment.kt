package com.example.lurkforreddit.model

import com.example.lurkforreddit.network.Created
import com.example.lurkforreddit.network.Thing
import com.example.lurkforreddit.network.Votable

data class Comment(
    override val created: Float,
    override val createdUtc: Float,
    override val ups: Int,
    override val downs: Int,
    val author: String,
    val body: String,
    val bodyHtml: String,
    val linkID: String,
    val replies: List<Thing>,
    val score: Int,
    val scoreHidden: Boolean,
    val subreddit: String,
    val subredditID: String,
    val distinguished: String?
): Created, Votable
