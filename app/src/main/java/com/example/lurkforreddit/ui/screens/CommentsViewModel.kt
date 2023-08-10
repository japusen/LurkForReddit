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
import com.example.lurkforreddit.model.CommentSort
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content
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
    val commentSort: CommentSort = CommentSort.BEST
)

class CommentsViewModel(
    private val redditApiRepository: RedditApiRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommentsUiState())
    val uiState: StateFlow<CommentsUiState> = _uiState.asStateFlow()

    val subreddit: String = savedStateHandle["subreddit"] ?: ""
    val article: String = savedStateHandle["article"] ?: ""

    init {
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
                            subreddit = subreddit,
                            article = article,
                            sort = currentState.commentSort
                        )
                        CommentsNetworkResponse.Success(
                            post = data.first.copy(
                                thumbnail = data.first.parseThumbnail(),
                                url = data.first.parseUrl()
                            ),
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

    /**
     * Change the sort of the comments and reload comments
     * @param sort the type of sort (best, top, new, controversial, q&a)
     * **/
    suspend fun setCommentSort(sort: CommentSort) {
        _uiState.update { currentState ->
            currentState.copy(
                commentSort = sort
            )
        }
        loadPostComments()
    }

    /**
     * Fetch more top-level comments and update the comment tree
     * **/
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
                            parentID = "t3_$article",
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

    /**
     * Search through comment tree to find a comment by id
     * @param id the id of the parent comment
     * @param comments the list of comments to search through
     * @return the comment or null if not found
     * **/
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

    /**
     * Fetch more replies to top-level or nested comments
     * and update the comment tree
     * @param parentID the id of the comment to fetch more replies for
     **/
    suspend fun getMoreComments(parentID: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val networkResponse = currentState.networkResponse
                if (networkResponse is CommentsNetworkResponse.Success) {

                    val parentComment = findParentComment(parentID, networkResponse.comments)
                    var more = parentComment?.more
                    val ids = more!!.getIDs(MORE_COMMENTS_AMOUNT)
                    more = more.copy(
                        count = more.count,
                        children = more.children
                    )

                    try {
                        val comments = networkResponse.comments
                        val newComments = redditApiRepository.getMoreComments(
                            linkID = article,
                            parentID = "t1_$parentID",
                            childrenIDs = ids,
                            sort = currentState.commentSort
                        )
                        Log.d("MORE", "new comments: $newComments")
                        currentState.copy(
                            networkResponse = networkResponse.copy(
                                comments = comments.map { comment ->
                                    comment.insert(parentID, newComments)
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
     * Fetch a list of specific replies by their ids
     * @param ids comma delimited list of ids to fetch
     * @return a list of comments
     * **/
    suspend fun moreComments(parentID: String, ids: String): List<Comment> {
        return try {
            redditApiRepository.getMoreComments(
                linkID = article,
                parentID = "t1_$parentID",
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
