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
import com.example.lurkforreddit.network.model.Content
import com.example.lurkforreddit.util.DuplicatesSort
import com.example.lurkforreddit.util.ListingSort
import com.example.lurkforreddit.util.TopSort
import com.example.lurkforreddit.util.UserListingSort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed interface ListingState {
    data class Success(
        val listingContent: Flow<PagingData<Content>>,
    ) : ListingState

    object Error : ListingState
    object Loading : ListingState
}

class HomeViewModel(
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {

    var listingState: ListingState by mutableStateOf(ListingState.Loading)
        private set

    private var subreddit: String by mutableStateOf("all")
    private var listingSort: ListingSort by mutableStateOf(ListingSort.HOT)
    private var topSort: TopSort? by mutableStateOf(null)

    init {
        viewModelScope.launch {
            redditApiRepository.initAccessToken()
            loadPosts()
        }
    }

    fun loadPosts() {
        viewModelScope.launch {
            listingState = try {
                ListingState.Success(
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
                ListingState.Error
            } catch (e: HttpException) {
                ListingState.Error
            }
        }
    }

    fun changeSubreddit(sub: String) {
        subreddit = sub
    }

    fun changeListingSort(sort: ListingSort) {
        listingSort = sort
    }

    fun changeTopSort(sort: TopSort) {
        topSort = sort
    }

    fun getPostDuplicates(
        subreddit: String,
        article: String,
        sort: DuplicatesSort = DuplicatesSort.NUMCOMMENTS
    ) {
        viewModelScope.launch {
            listingState = try {
                ListingState.Success(
                    redditApiRepository.getPostDuplicates(
                        subreddit,
                        article,
                        sort
                    ).cachedIn(viewModelScope)
                )
            } catch (e: IOException) {
                ListingState.Error
            } catch (e: HttpException) {
                ListingState.Error
            }
        }
    }


    fun getUserSubmissions(
        username: String,
        userListingSort: UserListingSort = UserListingSort.HOT,
        topSort: TopSort? = null
    ) {
        viewModelScope.launch {
            listingState = try {
                ListingState.Success(
                    redditApiRepository.getUserSubmissions(
                        username,
                        userListingSort,
                        topSort
                    ).cachedIn(viewModelScope)
                )
            } catch (e: IOException) {
                ListingState.Error
            } catch (e: HttpException) {
                ListingState.Error
            }
        }
    }


    fun getUserComments(
        username: String,
        userListingSort: UserListingSort = UserListingSort.HOT,
        topSort: TopSort? = null
    ) {
        viewModelScope.launch {
            listingState = try {
                ListingState.Success(
                    redditApiRepository.getUserComments(
                        username,
                        userListingSort,
                        topSort
                    ).cachedIn(viewModelScope)
                )
            } catch (e: IOException) {
                ListingState.Error
            } catch (e: HttpException) {
                ListingState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LurkApplication)
                val redditApiRepository = application.container.redditApiRepository
                HomeViewModel(redditApiRepository = redditApiRepository)
            }
        }
    }
}
