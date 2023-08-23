package com.example.lurkforreddit.domain.model

interface Created {
    val createdUtc: Float
}

interface Content {
    val id: String
    val author: String
    val distinguished: String?
    val score: Int
    val subreddit: String
}

interface Listing {
    val after: String?
    val children: List<Content>
}

interface CommentThreadItem {
    val visible: Boolean
    val depth: Int
}