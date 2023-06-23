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

sealed interface DetailsState {
    data class Success(
        val postData: Pair<PostApi, Pair<List<CommentApi>, MoreApi?>>,
    ) : DetailsState

    object Error : DetailsState
    object Loading : DetailsState
}

class PostDetailsViewModel(
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {
    var detailsState: DetailsState by mutableStateOf(DetailsState.Loading)
        private set

    private var commentSort: CommentSort by mutableStateOf(CommentSort.BEST)

    suspend fun loadPostComments(subreddit: String, article: String) {
        viewModelScope.launch {
            detailsState = try {
                DetailsState.Success(
                    redditApiRepository.getPostComments(
                        subreddit,
                        article,
                        commentSort
                    )
                )
            } catch (e: IOException) {
                DetailsState.Error
            } catch (e: HttpException) {
                DetailsState.Error
            }
        }
    }

    suspend fun changeCommentSort(
        subreddit: String,
        article: String,
        sort: CommentSort
    ) {
        commentSort = sort
        loadPostComments(subreddit, article)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LurkApplication)
                val redditApiRepository = application.container.redditApiRepository
                PostDetailsViewModel(redditApiRepository = redditApiRepository)
            }
        }
    }
}
