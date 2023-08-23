package com.example.lurkforreddit.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.lurkforreddit.LurkApplication
import com.example.lurkforreddit.domain.model.Content
import com.example.lurkforreddit.data.remote.model.PostDto
import com.example.lurkforreddit.domain.model.ListingSort
import com.example.lurkforreddit.domain.model.TopSort
import com.example.lurkforreddit.domain.repository.RedditApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
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
    val listingSort: ListingSort = ListingSort.HOT,
    val topSort: TopSort? = null,
)

class ListingViewModel(
    private val redditApiRepository: RedditApiRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListingUiState())
    val uiState: StateFlow<ListingUiState> = _uiState.asStateFlow()

    val subreddit: String = savedStateHandle["subreddit"] ?: "All"

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    networkResponse = try {
                        ListingNetworkResponse.Success(
                            redditApiRepository.getPosts(
                                subreddit = subreddit,
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LurkApplication)
                val redditApiRepository = application.container.redditApiRepository
                val savedStateHandle = createSavedStateHandle()

                ListingViewModel(
                    redditApiRepository = redditApiRepository,
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }
}
