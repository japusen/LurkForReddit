package com.example.lurkforreddit.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.lurkforreddit.LurkApplication
import com.example.lurkforreddit.data.RedditApiRepository
import com.example.lurkforreddit.model.Content
import com.example.lurkforreddit.model.SearchResult
import com.example.lurkforreddit.util.ListingSort
import com.example.lurkforreddit.util.TopSort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed interface ListingNetworkResponse {
    data class Success(
        val listingContent: Flow<PagingData<Content>>,
    ) : ListingNetworkResponse

    object Error : ListingNetworkResponse
    object Loading : ListingNetworkResponse
}

data class ListingUiState(
    val networkResponse: ListingNetworkResponse = ListingNetworkResponse.Loading,
    val subreddit: String = "All",
    val listingSort: ListingSort = ListingSort.HOT,
    val topSort: TopSort? = null,
    val isLoading: Boolean = false,
    val query: String = "",
    val searchResults: List<SearchResult> = listOf()
)

class ListingViewModel(
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListingUiState())
    val uiState: StateFlow<ListingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            redditApiRepository.initAccessToken()
            loadPosts()
        }
    }

    private suspend fun loadPosts() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    networkResponse = try {
                        ListingNetworkResponse.Success(
                            redditApiRepository.getPosts(
                                subreddit = currentState.subreddit,
                                sort = currentState.listingSort,
                                topSort = currentState.topSort
                            ).cachedIn(viewModelScope)
//                    .map{ pagingData ->
//                        pagingData.map { content -> ... }
//                    }
                        )
                    } catch (e: IOException) {
                        ListingNetworkResponse.Error
                    } catch (e: HttpException) {
                        ListingNetworkResponse.Error
                    },
                    isLoading = false,
                )
            }

        }
    }

    suspend fun setSubreddit(subreddit: String) {
        _uiState.update { currentState ->
            currentState.copy(
                subreddit = subreddit,
                isLoading = true
            )
        }
        loadPosts()
    }

    suspend fun setListingSort(sort: ListingSort, topSort: TopSort? = null) {
        _uiState.update { currentState ->
            currentState.copy(
                listingSort = sort,
                topSort = topSort,
                isLoading = true
            )
        }
        loadPosts()
    }

    fun setQuery(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                query = query,
            )
        }
    }

    fun clearQuery() {
        _uiState.update { currentState ->
            currentState.copy(
                query = "",
                searchResults = listOf()
            )
        }
    }

    suspend fun updateSearchResults() {
        _uiState.update { currentState ->
            currentState.copy(
                searchResults = redditApiRepository.subredditAutoComplete(currentState.query)
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LurkApplication)
                val redditApiRepository = application.container.redditApiRepository
                ListingViewModel(redditApiRepository = redditApiRepository)
            }
        }
    }
}
