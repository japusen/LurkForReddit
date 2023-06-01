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
import com.example.lurkforreddit.LurkApplication
import com.example.lurkforreddit.data.RedditApiRepository
import com.example.lurkforreddit.network.LinkApi
import com.example.lurkforreddit.network.ListingData
import com.example.lurkforreddit.util.CommentSort
import com.example.lurkforreddit.util.ListingSort
import com.example.lurkforreddit.util.TopSort
import com.example.lurkforreddit.util.UserListing
import com.example.lurkforreddit.util.UserSort
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed interface LurkUiState {
    data class Success(val data: ListingData) : LurkUiState
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
            getListing("all", ListingSort.HOT)
//            getListing("all", ListingSort.TOP, TopSort.MONTH)
//            getComments("all", "13ve8iq", CommentSort.QA)
//            getDuplicates("oddlysatisfying", "13vdm8v")
//            getUserSubmissions("theindependentonline",  UserSort.HOT)
//            getUserComments("theindependentonline",  UserSort.TOP, TopSort.DAY)
        }
    }

    fun getListing(
        subreddit: String,
        listingSort: ListingSort = ListingSort.HOT,
        topSort: TopSort? = null
    ) {
        viewModelScope.launch {
            lurkUiState = try {
                val listing = redditApiRepository.getListing(subreddit, listingSort, topSort)
                /* Map network model to ui model */
                val mappedListing = listing.children.map {
                    when (it) {
                        is LinkApi -> it.toPost()
                        else -> it
                    }
                }
                LurkUiState.Success(
                    data = listing
                )
            } catch (e: IOException) {
                LurkUiState.Error
            } catch (e: HttpException) {
                LurkUiState.Error
            }
        }
    }

    fun getComments(
        subreddit: String,
        article: String,
        sort: CommentSort = CommentSort.BEST
    ) {
        viewModelScope.launch {
            lurkUiState = try {
                LurkUiState.Success(
                    redditApiRepository.getComments(subreddit, article, sort)
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
        article: String
    ) {
        viewModelScope.launch {
            lurkUiState = try {
                LurkUiState.Success(
                    redditApiRepository.getDuplicates(subreddit, article)
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
        userSort: UserSort = UserSort.HOT,
        topSort: TopSort? = null
    ) {
        viewModelScope.launch {
            lurkUiState = try {
                LurkUiState.Success(
                    redditApiRepository.getUser(username, UserListing.SUBMITTED, userSort, topSort)
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
        userSort: UserSort = UserSort.HOT,
        topSort: TopSort? = null
    ) {
        viewModelScope.launch {
            lurkUiState = try {
                LurkUiState.Success(
                    redditApiRepository.getUser(username, UserListing.COMMENTS, userSort, topSort)
                )
            } catch (e: IOException) {
                LurkUiState.Error
            } catch (e: HttpException) {
                LurkUiState.Error
            }
        }
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