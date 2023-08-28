package com.example.lurkforreddit.ui.comments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lurkforreddit.domain.model.CommentSort
import com.example.lurkforreddit.domain.model.CommentThreadItem
import com.example.lurkforreddit.domain.model.Post
import com.example.lurkforreddit.domain.repository.CommentThreadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface CommentsNetworkResponse {
    data class Success(
        val post: Post,
        val commentThread: List<CommentThreadItem>
    ) : CommentsNetworkResponse

    object Error : CommentsNetworkResponse
    object Loading : CommentsNetworkResponse
}

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val commentThreadRepository: CommentThreadRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommentsUiState())
    val uiState: StateFlow<CommentsUiState> = _uiState.asStateFlow()

    val subreddit: String = savedStateHandle["subreddit"] ?: ""
    val article: String = savedStateHandle["article"] ?: ""

    init {
        loadPostComments()
    }

    private fun loadPostComments() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    networkResponse = try {
                        val data = commentThreadRepository.getCommentThread(
                            subreddit = subreddit,
                            article = article,
                            sort = currentState.commentSort
                        )
                        CommentsNetworkResponse.Success(
                            post = data.first,
                            commentThread = data.second
                        )
                    } catch (e: IOException) {
                        CommentsNetworkResponse.Error
                    } catch (e: HttpException) {
                        CommentsNetworkResponse.Error
                    }
                )
            }
        }
    }

    /**
     * Change the sort of the comments and reload comments
     * @param sort the type of sort (best, top, new, controversial, q&a)
     * **/
    fun setCommentSort(sort: CommentSort) {
        _uiState.update { currentState ->
            currentState.copy(
                commentSort = sort
            )
        }
        loadPostComments()
    }

    /**
     * Fetch more comments and update the comment tree
     * **/
    fun getMoreComments(index: Int) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    networkResponse = try {
                        val networkResponse = currentState.networkResponse
                        if (networkResponse is CommentsNetworkResponse.Success) {

                            val updatedThread = commentThreadRepository.addComments(
                                commentThread = networkResponse.commentThread.toMutableList(),
                                index = index,
                                linkID = article,
                                sort = currentState.commentSort
                            )
                            networkResponse.copy(
                                commentThread = updatedThread
                            )
                        } else {
                            CommentsNetworkResponse.Error
                        }
                    } catch (e: IOException) {
                        CommentsNetworkResponse.Error
                    } catch (e: HttpException) {
                        CommentsNetworkResponse.Error
                    }
                )
            }
        }
    }

    /**
     * Hides / Reveals child comments in the comment thread
     * @param index the index of the parent
     * @param depth the depth of the parent
     */
    fun changeCommentVisibility(show: Boolean, index: Int, depth: Int) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    networkResponse = try {
                        val networkResponse = currentState.networkResponse
                        if (networkResponse is CommentsNetworkResponse.Success) {

                            val updatedThread = commentThreadRepository.changeCommentVisibility(
                                commentThread = networkResponse.commentThread.toMutableList(),
                                show = show,
                                index = index,
                                depth = depth
                            )
                            networkResponse.copy(
                                commentThread = updatedThread
                            )
                        } else {
                            CommentsNetworkResponse.Error
                        }
                    } catch (e: IOException) {
                        CommentsNetworkResponse.Error
                    } catch (e: HttpException) {
                        CommentsNetworkResponse.Error
                    }
                )
            }
        }
    }

//    companion object {
//        val Factory: ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val application =
//                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LurkApplication)
//                val commentThreadRepository = application.container.commentThreadRepository
//                val savedStateHandle = createSavedStateHandle()
//                CommentsViewModel(
//                    commentThreadRepository = commentThreadRepository,
//                    savedStateHandle = savedStateHandle
//                )
//            }
//        }
//    }
}
