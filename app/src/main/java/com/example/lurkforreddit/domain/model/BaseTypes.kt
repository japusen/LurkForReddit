package com.example.lurkforreddit.domain.model

interface Created {
    val created: Float
    val createdUtc: Float
}

interface Content {
    val id: String
    val author: String
    val distinguished: String?
    val score: Int
    val subreddit: String
    val subredditID: String
}

interface Listing {
    val after: String?
    val before: String?
    val dist: Int?
    val children: List<Content>
}

interface CommentThreadItem {
    var visible: Boolean
    val name: String
    val id: String
    val parentID: String
    val depth: Int
}