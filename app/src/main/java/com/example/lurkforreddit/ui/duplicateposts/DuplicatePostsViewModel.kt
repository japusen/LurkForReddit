package com.example.lurkforreddit.ui.duplicateposts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.lurkforreddit.domain.model.DuplicatesSort
import com.example.lurkforreddit.domain.repository.DuplicatePostsRepository
import com.example.lurkforreddit.ui.subreddit.ListingNetworkResponse
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
class DuplicatePostsViewModel @Inject constructor(
    private val duplicatePostsRepository: DuplicatePostsRepository,
    savedStateHandle: SavedStateHandle
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


//    companion object {
//        val Factory: ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val application = (this[APPLICATION_KEY] as LurkApplication)
//                val duplicatePostsRepository = application.container.duplicatePostsRepository
//                val savedStateHandle = createSavedStateHandle()
//                DuplicatePostsViewModel(
//                    duplicatePostsRepository = duplicatePostsRepository,
//                    savedStateHandle = savedStateHandle
//                )
//            }
//        }
//    }
}
