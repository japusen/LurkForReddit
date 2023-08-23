package com.example.lurkforreddit.domain.repository

import com.example.lurkforreddit.domain.model.SearchResult

interface SearchResultsRepository {
    suspend fun getSearchResults(
        query: String,
    ): List<SearchResult>
}