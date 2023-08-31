package com.example.lurkforreddit.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["postId"], unique = true)])
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val postId: String,
    val author: String,
    val distinguished: String?,
    val score: Int,
    val subreddit: String,
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
)
