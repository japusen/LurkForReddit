package com.example.lurkforreddit.model

data class More(
    val name: String,
    val count: Int,
    val id: String,
    val parentID: String,
    val commentIDs: List<String>
)
