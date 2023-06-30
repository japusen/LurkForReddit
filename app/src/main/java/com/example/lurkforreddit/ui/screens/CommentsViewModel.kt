package com.example.lurkforreddit.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

sealed interface CommentsNetworkResponse {
    data class Success(
        val postData: Pair<Post, Pair<List<Comment>, More?>>,
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
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommentsUiState())
    val uiState: StateFlow<CommentsUiState> = _uiState.asStateFlow()

    suspend fun loadPostComments() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    networkResponse = try {
                        CommentsNetworkResponse.Success(
                            redditApiRepository.getPostComments(
                                subreddit = currentState.subreddit,
                                article = currentState.article,
                                sort = currentState.commentSort
                            )
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

    fun setSubreddit(subreddit: String) {
        _uiState.update { currentState ->
            currentState.copy(
                subreddit = subreddit
            )
        }
    }

    fun setArticle(article: String) {
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LurkApplication)
                val redditApiRepository = application.container.redditApiRepository
                CommentsViewModel(redditApiRepository = redditApiRepository)
            }
        }
    }
}
