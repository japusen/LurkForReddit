package com.example.lurkforreddit.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.cachedIn
import com.example.lurkforreddit.LurkApplication
import com.example.lurkforreddit.data.RedditApiRepository
import com.example.lurkforreddit.util.TopSort
import com.example.lurkforreddit.util.UserContentType
import com.example.lurkforreddit.util.UserListingSort
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


data class ProfileUiState(
    val networkResponse: ListingNetworkResponse = ListingNetworkResponse.Loading,
    val contentType: UserContentType = UserContentType.SUBMITTED,
    val userListingSort: UserListingSort = UserListingSort.HOT,
    val topSort: TopSort? = null,
)

class ProfileViewModel(
    private val redditApiRepository: RedditApiRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    val username: String = savedStateHandle["username"] ?: ""

    init {
        viewModelScope.launch {
            getUserContent()
        }
    }

    private suspend fun getUserContent() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    networkResponse = try {
                        ListingNetworkResponse.Success(
                            listingContent =
                            if (currentState.contentType == UserContentType.SUBMITTED) {
                                redditApiRepository.getUserSubmissions(
                                    username = username,
                                    sort = currentState.userListingSort,
                                    topSort = currentState.topSort
                                ).cachedIn(viewModelScope)
                            } else {
                                redditApiRepository.getUserComments(
                                    username = username,
                                    sort = currentState.userListingSort,
                                    topSort = currentState.topSort
                                ).cachedIn(viewModelScope)
                            }
                        )
                    } catch (e: IOException) {
                        ListingNetworkResponse.Error
                    } catch (e: HttpException) {
                        ListingNetworkResponse.Error
                    }
                )
            }
        }
    }

    /**
     * Change the content type and load the content
     * @param contentType the type of profile content to load (submissions or comments)
     * **/
    suspend fun setContentType(contentType: UserContentType) {
        _uiState.update { currentState ->
            currentState.copy(
                contentType = contentType,
            )
        }
        getUserContent()
    }

    /**
     * Change the sort type
     * @param sort the type of sort (hot, new, top, controversial)
     * @param topSort the time frame if the sort is top (hour, day, week, month, year, all)
     * **/
    suspend fun setListingSort(sort: UserListingSort, topSort: TopSort? = null) {
        _uiState.update { currentState ->
            currentState.copy(
                userListingSort = sort,
                topSort = topSort,
            )
        }
        getUserContent()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LurkApplication)
                val redditApiRepository = application.container.redditApiRepository
                val savedStateHandle = createSavedStateHandle()
                ProfileViewModel(
                    redditApiRepository = redditApiRepository,
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }
}
