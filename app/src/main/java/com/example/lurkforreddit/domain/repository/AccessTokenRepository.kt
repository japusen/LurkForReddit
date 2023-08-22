package com.example.lurkforreddit.domain.repository

interface AccessTokenRepository {
    suspend fun getAccessToken(): String
}