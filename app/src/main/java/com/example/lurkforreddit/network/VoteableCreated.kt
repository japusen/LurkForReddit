package com.example.lurkforreddit.network

interface Votable {
    val ups: Int
    val downs: Int
}

interface Created {
    val created: Float
    val createdUtc: Float
}