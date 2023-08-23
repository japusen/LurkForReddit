package com.example.lurkforreddit.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.lurkforreddit.LurkApplication
import com.example.lurkforreddit.data.remote.model.PostDto
import com.example.lurkforreddit.data.remote.model.SearchResultDto
import com.example.lurkforreddit.domain.model.ListingSort
import com.example.lurkforreddit.domain.model.TopSort
import com.example.lurkforreddit.domain.repository.PostRepository
import com.example.lurkforreddit.domain.repository.SearchResultsRepository
import com.example.lurkforreddit.ui.subreddit.ListingNetworkResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


data class HomeUiState(
    val networkResponse: ListingNetworkResponse = ListingNetworkResponse.Loading,
    val subreddit: String = "All",
    val listingSort: ListingSort = ListingSort.HOT,
    val topSort: TopSort? = null,
    val query: String = "",
    val searchResultDtos: List<SearchResultDto> = listOf()
)

class HomeViewModel(
    private val postRepository: PostRepository,
    private val searchResultsRepository: SearchResultsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    networkResponse = try {
                        ListingNetworkResponse.Success(
                            postRepository.getPosts(
                                subreddit = currentState.subreddit,
                                sort = currentState.listingSort,
                                topSort = currentState.topSort
                            )
                                .map { pagingData ->
                                    pagingData.map { content ->
                                        if (content is PostDto)
                                            content.copy(
                                                thumbnail = content.parseThumbnail(),
                                                url = content.parseUrl()
                                            )
                                        else
                                            content
                                    }
                                }
                                .cachedIn(viewModelScope)
                        )
                    } catch (e: IOException) {
                        ListingNetworkResponse.Error
                    } catch (e: HttpException) {
                        ListingNetworkResponse.Error
                    }
                )
            }

        }
    }

    /**
     * Change the current subreddit and reload posts
     * @param subreddit subreddit name
     *  **/
    fun setSubreddit(subreddit: String) {
        _uiState.update { currentState ->
            currentState.copy(
                subreddit = subreddit,
            )
        }
        loadPosts()
    }

    /**
     * Change the sort type and reload posts
     * @param sort the type of sort (hot, rising, new, top)
     * @param topSort the time frame if the sort is top (hour, day, week, month, year, all)
     * **/
    fun setListingSort(sort: ListingSort, topSort: TopSort? = null) {
        _uiState.update { currentState ->
            currentState.copy(
                listingSort = sort,
                topSort = topSort,
            )
        }
        loadPosts()
    }

    /**
     * Update the search query
     * @param query the search query
     */
    fun setQuery(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                query = query,
            )
        }
    }

    /**
     * Clear the search query
     */
    fun clearQuery() {
        _uiState.update { currentState ->
            currentState.copy(
                query = "",
                searchResultDtos = listOf()
            )
        }
    }

    /**
     * Fetch search results for the current query
     */
    fun updateSearchResults() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    searchResultDtos = searchResultsRepository.getSearchResults(currentState.query)
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LurkApplication)
                val postRepository = application.container.postRepository
                val searchResultRepository = application.container.searchResultsRepository

                HomeViewModel(
                    postRepository = postRepository,
                    searchResultsRepository = searchResultRepository
                )
            }
        }
    }
}
