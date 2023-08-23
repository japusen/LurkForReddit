package com.example.lurkforreddit.data.repository

import com.example.lurkforreddit.data.json.parseSearchResults
import com.example.lurkforreddit.data.remote.RedditApiService
import com.example.lurkforreddit.data.remote.model.SearchResultDto
import com.example.lurkforreddit.domain.repository.AccessTokenRepository
import com.example.lurkforreddit.domain.repository.SearchResultsRepository

class SearchResultsRepositoryImpl(
    private val accessTokenRepository: AccessTokenRepository,
    private val redditApiService: RedditApiService
): SearchResultsRepository {

    /**
     * Network call to get subreddit and usernames similar to the search query
     * @param query the query to perform search on
     * @return a list of SearchResults for the given query
     */
    override suspend fun getSearchResults(query: String): List<SearchResultDto> {

        val tokenHeader = accessTokenRepository.getAccessToken()

        val response = redditApiService.fetchSearchResults(
            tokenHeader,
            query
        )

        return parseSearchResults(response)
    }
}