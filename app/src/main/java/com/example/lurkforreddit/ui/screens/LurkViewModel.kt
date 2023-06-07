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
import com.example.lurkforreddit.LurkApplication
import com.example.lurkforreddit.data.RedditApiRepository
import com.example.lurkforreddit.network.CommentApi
import com.example.lurkforreddit.network.Content
import com.example.lurkforreddit.network.MoreApi
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
//            getListing("all", ListingSort.HOT)
//            getListing("all", ListingSort.TOP, TopSort.ALL)
//            getDuplicates("oddlysatisfying", "13vdm8v")
//            getUserSubmissions("theindependentonline",  UserListingSort.HOT)
            getUserComments("theindependentonline", UserListingSort.TOP, TopSort.ALL)
//            getPostComments("nba", "13ve8iq", CommentSort.BEST)
        }
    }

    fun getListing(
        subreddit: String,
        listingSort: ListingSort = ListingSort.HOT,
        topSort: TopSort? = null
    ) {
        viewModelScope.launch {
            lurkUiState = try {
                LurkUiState.Success(
                    redditApiRepository.getListing(
                        subreddit,
                        listingSort,
                        topSort
                    )
                )
            } catch (e: IOException) {
                LurkUiState.Error
            } catch (e: HttpException) {
                LurkUiState.Error
            }
        }
    }

    fun getDuplicates(
        subreddit: String,
        article: String,
        sort: DuplicatesSort = DuplicatesSort.NUMCOMMENTS
    ) {
        viewModelScope.launch {
            lurkUiState = try {
                LurkUiState.Success(
                    redditApiRepository.getDuplicates(
                        subreddit,
                        article,
                        sort
                    )
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
                    )
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
                    )
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