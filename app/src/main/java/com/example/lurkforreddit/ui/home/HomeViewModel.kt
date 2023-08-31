package com.example.lurkforreddit.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.lurkforreddit.domain.model.ListingSort
import com.example.lurkforreddit.domain.model.Post
import com.example.lurkforreddit.domain.util.NetworkResponse
import com.example.lurkforreddit.domain.model.TopSort
import com.example.lurkforreddit.domain.repository.PostRepository
import com.example.lurkforreddit.domain.repository.SearchResultsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
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
                        NetworkResponse.Success(
                            postRepository.getPosts(
                                subreddit = currentState.subreddit,
                                sort = currentState.listingSort,
                                topSort = currentState.topSort
                            ).cachedIn(viewModelScope)
                        )
                    } catch (e: IOException) {
                        NetworkResponse.Error("Could not make network request")
                    } catch (e: HttpException) {
                        NetworkResponse.Error("Request Failed")
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
                searchResult = listOf()
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
                    searchResult = searchResultsRepository.getSearchResults(currentState.query)
                )
            }
        }
    }

    /**
     * Get post history
     */
    fun getPostHistory(): Flow<List<Post>> {
        return postRepository.getPostHistory()
    }

    /**
     * Save viewed post to history
     * @param post the post to save
     */
    fun savePostToHistory(post: Post) {
        viewModelScope.launch {
            postRepository.savePostToHistory(post)
        }
    }

    /**
     * Clear the viewed history
     */
    fun clearPostHistory() {
        viewModelScope.launch {
            postRepository.clearPostHistory()
        }
    }

//    companion object {
//        val Factory: ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val application = (this[APPLICATION_KEY] as LurkApplication)
//                val postRepository = application.container.postRepository
//                val searchResultRepository = application.container.searchResultsRepository
//
//                HomeViewModel(
//                    postRepository = postRepository,
//                    searchResultsRepository = searchResultRepository
//                )
//            }
//        }
//    }
}
