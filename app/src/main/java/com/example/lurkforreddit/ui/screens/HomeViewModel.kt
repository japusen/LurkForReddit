package com.example.lurkforreddit.ui.screens

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed interface ListingNetworkRequest {
    data class Success(
        val listingContent: Flow<PagingData<Content>>,
    ) : ListingNetworkRequest

    object Error : ListingNetworkRequest
    object Loading : ListingNetworkRequest
}

data class HomeUiState(
    val networkResponse: ListingNetworkRequest = ListingNetworkRequest.Loading,
    val subreddit: String = "all",
    val listingSort: ListingSort = ListingSort.HOT,
    val topSort: TopSort? = null
)

class HomeViewModel(
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            redditApiRepository.initAccessToken()
            loadPosts()
        }
    }

    private suspend fun loadPosts() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    networkResponse = try {
                        ListingNetworkRequest.Success(
                            redditApiRepository.getPosts(
                                subreddit = currentState.subreddit,
                                sort = currentState.listingSort,
                                topSort = currentState.topSort
                            ).cachedIn(viewModelScope)
//                    .map{ pagingData ->
//                        pagingData.map { content -> ... }
//                    }
                        )
                    } catch (e: IOException) {
                        ListingNetworkRequest.Error
                    } catch (e: HttpException) {
                        ListingNetworkRequest.Error
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

    fun setListingSort(sort: ListingSort) {
        _uiState.update { currentState ->
            currentState.copy(
                listingSort = sort
            )
        }
    }

    fun setTopSort(sort: TopSort?) {
        _uiState.update { currentState ->
            currentState.copy(
                topSort = sort
            )
        }
    }


//    fun getPostDuplicates(
//        subreddit: String,
//        article: String,
//        sort: DuplicatesSort = DuplicatesSort.NUMCOMMENTS
//    ) {
//        viewModelScope.launch {
//            listingState = try {
//                ListingNetworkRequest.Success(
//                    redditApiRepository.getPostDuplicates(
//                        subreddit,
//                        article,
//                        sort
//                    ).cachedIn(viewModelScope)
//                )
//            } catch (e: IOException) {
//                ListingNetworkRequest.Error
//            } catch (e: HttpException) {
//                ListingNetworkRequest.Error
//            }
//        }
//    }
//
//
//    fun getUserSubmissions(
//        username: String,
//        userListingSort: UserListingSort = UserListingSort.HOT,
//        topSort: TopSort? = null
//    ) {
//        viewModelScope.launch {
//            listingState = try {
//                ListingNetworkRequest.Success(
//                    redditApiRepository.getUserSubmissions(
//                        username,
//                        userListingSort,
//                        topSort
//                    ).cachedIn(viewModelScope)
//                )
//            } catch (e: IOException) {
//                ListingNetworkRequest.Error
//            } catch (e: HttpException) {
//                ListingNetworkRequest.Error
//            }
//        }
//    }
//
//
//    fun getUserComments(
//        username: String,
//        userListingSort: UserListingSort = UserListingSort.HOT,
//        topSort: TopSort? = null
//    ) {
//        viewModelScope.launch {
//            listingState = try {
//                ListingNetworkRequest.Success(
//                    redditApiRepository.getUserComments(
//                        username,
//                        userListingSort,
//                        topSort
//                    ).cachedIn(viewModelScope)
//                )
//            } catch (e: IOException) {
//                ListingNetworkRequest.Error
//            } catch (e: HttpException) {
//                ListingNetworkRequest.Error
//            }
//        }
//    }

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
