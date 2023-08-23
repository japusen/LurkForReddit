package com.example.lurkforreddit.data.mappers

import com.example.lurkforreddit.data.remote.model.SearchResultDto
import com.example.lurkforreddit.domain.model.SearchResult

fun SearchResultDto.toSearchResult(): SearchResult {
    return SearchResult(name, communityIcon, icon)
}