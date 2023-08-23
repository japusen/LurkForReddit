package com.example.lurkforreddit.domain.repository

import com.example.lurkforreddit.data.remote.model.SearchResultDto

interface SearchResultsRepository {
    suspend fun getSearchResults(
        query: String,
    ): List<SearchResultDto>
}