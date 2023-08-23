package com.example.lurkforreddit.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.lurkforreddit.data.pagingsource.ListingPagingSource
import com.example.lurkforreddit.data.remote.RedditApiService
import com.example.lurkforreddit.domain.model.Content
import com.example.lurkforreddit.domain.model.PagingListing
import com.example.lurkforreddit.domain.model.TopSort
import com.example.lurkforreddit.domain.model.UserListingSort
import com.example.lurkforreddit.domain.repository.AccessTokenRepository
import com.example.lurkforreddit.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class ProfileRepositoryImpl(
    private val accessTokenRepository: AccessTokenRepository,
    private val redditApiService: RedditApiService
): ProfileRepository {

    /**
     * Network call to fetch user submissions
     * @param username the name of the user
     * @param sort the type of sort (number of comments, new)
     * @param topSort the time frame if the sort is top (hour, day, week, month, year, all)
     * @return Flow of PagingData Content
     */
    override suspend fun getUserSubmissions(
        username: String,
        sort: UserListingSort,
        topSort: TopSort?
    ): Flow<PagingData<Content>> {

        val tokenHeader = accessTokenRepository.getAccessToken()

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ListingPagingSource(
                    listingType = PagingListing.USERSUBMISSIONS,
                    service = redditApiService,
                    tokenHeader = tokenHeader,
                    username = username,
                    sort = sort.value,
                    topSort = topSort?.value
                )
            }
        ).flow
    }


    /**
     * Network call to fetch user comments
     * @param username the name of the user
     * @param sort the type of sort (number of comments, new)
     * @param topSort the time frame if the sort is top (hour, day, week, month, year, all)
     * @return Flow of PagingData Content
     */
    override suspend fun getUserComments(
        username: String,
        sort: UserListingSort,
        topSort: TopSort?
    ): Flow<PagingData<Content>> {

        val tokenHeader = accessTokenRepository.getAccessToken()

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ListingPagingSource(
                    listingType = PagingListing.USERCOMMENTS,
                    service = redditApiService,
                    tokenHeader = tokenHeader,
                    username = username,
                    sort = sort.value,
                    topSort = topSort?.value
                )
            }
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 25
    }
}