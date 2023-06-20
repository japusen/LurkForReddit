package com.example.lurkforreddit.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lurkforreddit.LurkApplication
import com.example.lurkforreddit.data.RedditApiRepository
import com.example.lurkforreddit.network.model.CommentApi
import com.example.lurkforreddit.network.model.MoreApi
import com.example.lurkforreddit.network.model.PostApi
import com.example.lurkforreddit.util.CommentSort
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface CommentState {
    data class Success(
        val postData: Pair<PostApi, Pair<List<CommentApi>, MoreApi?>>,
    ) : CommentState

    object Error : CommentState
    object Loading : CommentState
}

class PostCommentsViewModel(
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {
    var commentState: CommentState by mutableStateOf(CommentState.Loading)
        private set

    private var subreddit: String by mutableStateOf("")
    private var article: String by mutableStateOf("")
    private var commentSort: CommentSort by mutableStateOf(CommentSort.BEST)

    suspend fun loadPostComments() {
        viewModelScope.launch {
            commentState = try {
                CommentState.Success(
                    redditApiRepository.getPostComments(
                        subreddit,
                        article,
                        commentSort
                    )
                )
            } catch (e: IOException) {
                CommentState.Error
            } catch (e: HttpException) {
                CommentState.Error
            }
        }
    }

    fun changeSubreddit(sub: String) {
        subreddit = sub
    }

    fun changeArticle(post: String) {
        article = post
    }

    fun changeCommentSort(sort: CommentSort) {
        commentSort = sort
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LurkApplication)
                val redditApiRepository = application.container.redditApiRepository
                PostCommentsViewModel(redditApiRepository = redditApiRepository)
            }
        }
    }
}
