package com.example.lurkforreddit.ui.duplicateposts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.lurkforreddit.LurkApplication
import com.example.lurkforreddit.domain.model.DuplicatesSort
import com.example.lurkforreddit.data.remote.model.PostDto
import com.example.lurkforreddit.domain.model.Post
import com.example.lurkforreddit.domain.repository.DuplicatePostsRepository
import com.example.lurkforreddit.ui.subreddit.ListingNetworkResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


data class DuplicatesUiState(
    val networkResponse: ListingNetworkResponse = ListingNetworkResponse.Loading,
    val sort: DuplicatesSort = DuplicatesSort.NUMCOMMENTS,
)

class DuplicatePostsViewModel(
    private val duplicatePostsRepository: DuplicatePostsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DuplicatesUiState())
    val uiState: StateFlow<DuplicatesUiState> = _uiState.asStateFlow()

    private val subreddit: String = savedStateHandle["subreddit"] ?: ""
    private val article: String = savedStateHandle["article"] ?: ""

    init {
        loadDuplicates()
    }
    private fun loadDuplicates() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    networkResponse = try {
                        ListingNetworkResponse.Success(
                            duplicatePostsRepository.getDuplicatePosts(
                                subreddit = subreddit,
                                article = article,
                                sort = currentState.sort,
                            ).cachedIn(viewModelScope)
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
     * Change the sort type and load the duplicate posts
     * @param sort the type of sort (number of comments, new)
     * **/
    fun setSort(sort: DuplicatesSort) {
        _uiState.update { currentState ->
            currentState.copy(
                sort = sort,
            )
        }
        loadDuplicates()
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LurkApplication)
                val duplicatePostsRepository = application.container.duplicatePostsRepository
                val savedStateHandle = createSavedStateHandle()
                DuplicatePostsViewModel(
                    duplicatePostsRepository = duplicatePostsRepository,
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }
}
