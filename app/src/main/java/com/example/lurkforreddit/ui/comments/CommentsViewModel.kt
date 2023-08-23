package com.example.lurkforreddit.ui.comments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lurkforreddit.LurkApplication
import com.example.lurkforreddit.data.remote.model.PostDto
import com.example.lurkforreddit.domain.model.Comment
import com.example.lurkforreddit.domain.model.CommentSort
import com.example.lurkforreddit.domain.model.CommentThreadItem
import com.example.lurkforreddit.domain.model.More
import com.example.lurkforreddit.domain.repository.CommentThreadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

private const val MORE_COMMENTS_AMOUNT = 50

sealed interface CommentsNetworkResponse {
    data class Success(
        val postDto: PostDto,
        val commentThread: List<CommentThreadItem>
    ) : CommentsNetworkResponse

    object Error : CommentsNetworkResponse
    object Loading : CommentsNetworkResponse
}

data class CommentsUiState(
    val networkResponse: CommentsNetworkResponse = CommentsNetworkResponse.Loading,
    val commentSort: CommentSort = CommentSort.BEST
)

class CommentsViewModel(
    private val commentThreadRepository: CommentThreadRepository,
    private val savedStateHandle: SavedStateHandle
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
                            postDto = data.first.copy(
                                thumbnail = data.first.parseThumbnail(),
                                url = data.first.parseUrl()
                            ),
                            commentThread = data.second,
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
                val networkResponse = currentState.networkResponse
                if (networkResponse is CommentsNetworkResponse.Success) {
                    val commentThread = networkResponse.commentThread
                    try {
                        val more = commentThread[index] as More
                        val ids = more.getIDs(MORE_COMMENTS_AMOUNT)

                        val newComments = commentThreadRepository.getMoreComments(
                            linkID = article,
                            parentID = "t3_$article",
                            childrenIDs = ids,
                            sort = currentState.commentSort
                        )
                        currentState.copy(
                            networkResponse = networkResponse.copy(
                                commentThread = commentThread.toMutableList().apply {
                                    if (more.children.isEmpty())
                                        removeAt(index)
                                    addAll(index, newComments)
                                }
                            )
                        )
                    } catch (e: IOException) {
                        currentState.copy(
                            networkResponse = CommentsNetworkResponse.Error
                        )
                    } catch (e: HttpException) {
                        currentState.copy(
                            networkResponse = CommentsNetworkResponse.Error
                        )
                    }
                } else
                    currentState
            }
        }
    }

    /**
     * Hides / Reveals child comments in the comment thread
     * @param start the index of the parent
     * @param depth the depth of the parent
     */
    fun changeCommentVisibility(visible: Boolean, start: Int, depth: Int) {
        _uiState.update { currentState ->
            val networkResponse = currentState.networkResponse
            if (networkResponse is CommentsNetworkResponse.Success) {
                val commentThread = networkResponse.commentThread
                currentState.copy(
                    networkResponse = networkResponse.copy(
                        commentThread = commentThread.toMutableList().apply {
                            var index = start + 1
                            while (index < size) {
                                val item = elementAt(index)
                                if (item.depth > depth) {
                                    when (item) {
                                        is Comment -> set(index, item.copy(visible = visible))
                                        is More -> set(index, item.copy(visible = visible))
                                    }
                                    index += 1
                                }
                                else
                                    break
                            }
                        }
                    )
                )
            }
            else
                currentState
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LurkApplication)
                val commentThreadRepository = application.container.commentThreadRepository
                val savedStateHandle = createSavedStateHandle()
                CommentsViewModel(
                    commentThreadRepository = commentThreadRepository,
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }
}
