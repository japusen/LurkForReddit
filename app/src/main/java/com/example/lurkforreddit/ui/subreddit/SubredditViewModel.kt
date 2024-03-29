package com.example.lurkforreddit.ui.subreddit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.lurkforreddit.domain.model.ListingSort
import com.example.lurkforreddit.domain.model.Post
import com.example.lurkforreddit.domain.util.NetworkResponse
import com.example.lurkforreddit.domain.model.TopSort
import com.example.lurkforreddit.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class SubredditViewModel @Inject constructor(
    private val postRepository: PostRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubredditUiState())
    val uiState: StateFlow<SubredditUiState> = _uiState.asStateFlow()

    val subreddit: String = savedStateHandle["subreddit"] ?: "All"

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
                                subreddit = subreddit,
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
     * Save viewed post to history
     * @param post the post to save
     */
    fun savePostToHistory(post: Post) {
        viewModelScope.launch {
            postRepository.savePostToHistory(post)
        }
    }

//    companion object {
//        val Factory: ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val application = (this[APPLICATION_KEY] as LurkApplication)
//                val postRepository = application.container.postRepository
//                val savedStateHandle = createSavedStateHandle()
//
//                SubredditViewModel(
//                    postRepository = postRepository,
//                    savedStateHandle = savedStateHandle
//                )
//            }
//        }
//    }
}
