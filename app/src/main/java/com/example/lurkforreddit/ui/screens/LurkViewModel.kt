package com.example.lurkforreddit.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.lurkforreddit.LurkApplication
import com.example.lurkforreddit.data.RedditApiRepository
import com.example.lurkforreddit.network.model.CommentApi
import com.example.lurkforreddit.network.model.Content
import com.example.lurkforreddit.network.model.MoreApi
import com.example.lurkforreddit.util.CommentSort
import com.example.lurkforreddit.util.DuplicatesSort
import com.example.lurkforreddit.util.ListingSort
import com.example.lurkforreddit.util.TopSort
import com.example.lurkforreddit.util.UserListingSort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed interface LurkUiState {
    data class Success(
        val feedContent: Flow<PagingData<Content>>,
    ) : LurkUiState

    object Error : LurkUiState
    object Loading : LurkUiState
}

class LurkViewModel(
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {

    var lurkUiState: LurkUiState by mutableStateOf(LurkUiState.Loading)
        private set

    init {
        viewModelScope.launch {
            redditApiRepository.initAccessToken()
            getPosts("all", ListingSort.HOT)
//            getPosts("all", ListingSort.TOP, TopSort.ALL)
//            getPostDuplicates("oddlysatisfying", "13vdm8v")
//            getUserSubmissions("theindependentonline",  UserListingSort.HOT)
//            getUserComments("theindependentonline", UserListingSort.TOP, TopSort.ALL)
//            getPostComments("nba", "13ve8iq", CommentSort.BEST)
        }
    }

    fun getPosts(
        subreddit: String,
        listingSort: ListingSort = ListingSort.HOT,
        topSort: TopSort? = null
    ) {
        viewModelScope.launch {
            lurkUiState = try {
                LurkUiState.Success(
                    redditApiRepository.getPosts(
                        subreddit,
                        listingSort,
                        topSort
                    ).cachedIn(viewModelScope)
//                    .map{ pagingData ->
//                        pagingData.map { content -> ... }
//                    }
                )
            } catch (e: IOException) {
                LurkUiState.Error
            } catch (e: HttpException) {
                LurkUiState.Error
            }
        }
    }

    fun getPostDuplicates(
        subreddit: String,
        article: String,
        sort: DuplicatesSort = DuplicatesSort.NUMCOMMENTS
    ) {
        viewModelScope.launch {
            lurkUiState = try {
                LurkUiState.Success(
                    redditApiRepository.getPostDuplicates(
                        subreddit,
                        article,
                        sort
                    ).cachedIn(viewModelScope)
                )
            } catch (e: IOException) {
                LurkUiState.Error
            } catch (e: HttpException) {
                LurkUiState.Error
            }
        }
    }


    fun getUserSubmissions(
        username: String,
        userListingSort: UserListingSort = UserListingSort.HOT,
        topSort: TopSort? = null
    ) {
        viewModelScope.launch {
            lurkUiState = try {
                LurkUiState.Success(
                    redditApiRepository.getUserSubmissions(
                        username,
                        userListingSort,
                        topSort
                    ).cachedIn(viewModelScope)
                )
            } catch (e: IOException) {
                LurkUiState.Error
            } catch (e: HttpException) {
                LurkUiState.Error
            }
        }
    }


    fun getUserComments(
        username: String,
        userListingSort: UserListingSort = UserListingSort.HOT,
        topSort: TopSort? = null
    ) {
        viewModelScope.launch {
            lurkUiState = try {
                LurkUiState.Success(
                    redditApiRepository.getUserComments(
                        username,
                        userListingSort,
                        topSort
                    ).cachedIn(viewModelScope)
                )
            } catch (e: IOException) {
                LurkUiState.Error
            } catch (e: HttpException) {
                LurkUiState.Error
            }
        }
    }

    suspend fun getPostComments(
        subreddit: String,
        article: String,
        sort: CommentSort = CommentSort.BEST
    ): Pair<List<CommentApi>, MoreApi?> {
        return redditApiRepository.getPostComments(
            subreddit,
            article,
            sort
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LurkApplication)
                val redditApiRepository = application.container.redditApiRepository
                LurkViewModel(redditApiRepository = redditApiRepository)
            }
        }
    }

}