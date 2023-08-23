package com.example.lurkforreddit.domain.repository

import com.example.lurkforreddit.data.remote.model.SearchResultDto

interface SearchResultRepository {
    suspend fun getSearchResults(
        query: String,
    ): List<SearchResultDto>
}