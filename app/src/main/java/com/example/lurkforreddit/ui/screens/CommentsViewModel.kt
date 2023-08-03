package com.example.lurkforreddit.ui.screens

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lurkforreddit.LurkApplication
import com.example.lurkforreddit.data.RedditApiRepository
import com.example.lurkforreddit.model.Comment
import com.example.lurkforreddit.model.More
import com.example.lurkforreddit.model.Post
import com.example.lurkforreddit.util.CommentSort
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
        val post: Post,
        val comments: List<Comment>,
        val more: More?
    ) : CommentsNetworkResponse

    object Error : CommentsNetworkResponse
    object Loading : CommentsNetworkResponse
}

data class CommentsUiState(
    val networkResponse: CommentsNetworkResponse = CommentsNetworkResponse.Loading,
    val subreddit: String = "",
    val article: String = "",
    val commentSort: CommentSort = CommentSort.BEST
)

class CommentsViewModel(
    private val redditApiRepository: RedditApiRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommentsUiState())
    val uiState: StateFlow<CommentsUiState> = _uiState.asStateFlow()

    private val subreddit: String = savedStateHandle["subreddit"] ?: ""
    private val article: String = savedStateHandle["article"] ?: ""

    init {
        setSubreddit(subreddit)
        setArticle(article)
        viewModelScope.launch {
            loadPostComments()
        }
    }

    private suspend fun loadPostComments() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    networkResponse = try {
                        val data = redditApiRepository.getPostComments(
                            subreddit = currentState.subreddit,
                            article = currentState.article,
                            sort = currentState.commentSort
                        )
                        CommentsNetworkResponse.Success(
                            post = data.first,
                            comments = data.second.first,
                            more = data.second.second
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

    private fun setSubreddit(subreddit: String) {
        _uiState.update { currentState ->
            currentState.copy(
                subreddit = subreddit
            )
        }
    }

    private fun setArticle(article: String) {
        _uiState.update { currentState ->
            currentState.copy(
                article = article
            )
        }
    }

    suspend fun setCommentSort(sort: CommentSort) {
        _uiState.update { currentState ->
            currentState.copy(
                commentSort = sort
            )
        }
        loadPostComments()
    }

    suspend fun getMoreComments() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val networkResponse = currentState.networkResponse
                if (networkResponse is CommentsNetworkResponse.Success && networkResponse.more != null) {
                    val more = networkResponse.more
                    val ids = more.getIDs(MORE_COMMENTS_AMOUNT)

                    try {
                        val comments = networkResponse.comments
                        val newComments = redditApiRepository.getMoreComments(
                            linkID = article,
                            childrenIDs = ids,
                            sort = currentState.commentSort
                        )
                        currentState.copy(
                            networkResponse = networkResponse.copy(
                                comments = comments + newComments,
                                more = more.copy(
                                    count = more.count,
                                    children = more.children
                                )
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

    private fun findParentComment(id: String, comments: List<Comment>): Comment? {
        if (comments.isEmpty())
            return null

        for (comment in comments) {
            if (comment.contents?.id == id)
                return comment
            val checkChild = findParentComment(id, comment.replies)
            if (checkChild != null)
                return checkChild
        }

        return null
    }

    suspend fun getMoreComments(parentID: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val networkResponse = currentState.networkResponse
                if (networkResponse is CommentsNetworkResponse.Success) {

                    val parentComment = findParentComment(parentID, networkResponse.comments)
                    val more = parentComment?.more
                    val ids = more!!.getIDs(MORE_COMMENTS_AMOUNT)

                    try {
                        val comments = networkResponse.comments
                        val newComments = redditApiRepository.getMoreComments(
                            linkID = article,
                            childrenIDs = ids,
                            sort = currentState.commentSort
                        )
                        parentComment.replies.addAll(newComments)
                        Log.d("MORE", "added")
                        currentState.copy(
                            networkResponse = networkResponse.copy(
                                comments = comments
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

    suspend fun moreComments(ids: String): List<Comment> {
        return try {
            redditApiRepository.getMoreComments(
                linkID = article,
                childrenIDs = ids,
                sort = _uiState.value.commentSort
            )
        }catch (e: IOException) {
            listOf()
        } catch (e: HttpException) {
            listOf()
        }

    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LurkApplication)
                val redditApiRepository = application.container.redditApiRepository
                val savedStateHandle = createSavedStateHandle()
                CommentsViewModel(
                    redditApiRepository = redditApiRepository,
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }
}
