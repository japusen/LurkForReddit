package com.example.lurkforreddit.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.cachedIn
import com.example.lurkforreddit.LurkApplication
import com.example.lurkforreddit.data.RedditApiRepository
import com.example.lurkforreddit.util.DuplicatesSort
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


data class DuplicatesUiState(
    val networkResponse: ListingNetworkResponse = ListingNetworkResponse.Loading,
    val subreddit: String = "",
    val article: String = "",
    val sort: DuplicatesSort = DuplicatesSort.NUMCOMMENTS,
)

class DuplicatesViewModel(
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DuplicatesUiState())
    val uiState: StateFlow<DuplicatesUiState> = _uiState.asStateFlow()

    suspend fun loadDuplicates() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    networkResponse = try {
                        ListingNetworkResponse.Success(
                            redditApiRepository.getPostDuplicates(
                                subreddit = currentState.subreddit,
                                article = currentState.article,
                                sort = currentState.sort,
                            ).cachedIn(viewModelScope)
//                    .map{ pagingData ->
//                        pagingData.map { content -> ... }
//                    }
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

    fun setSubreddit(subreddit: String) {
        _uiState.update { currentState ->
            currentState.copy(
                subreddit = subreddit,
            )
        }
    }

    fun setArticle(article: String) {
        _uiState.update { currentState ->
            currentState.copy(
                article = article,
            )
        }
    }

    suspend fun setSort(sort: DuplicatesSort) {
        _uiState.update { currentState ->
            currentState.copy(
                sort = sort,
            )
        }
        loadDuplicates()
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LurkApplication)
                val redditApiRepository = application.container.redditApiRepository
                DuplicatesViewModel(redditApiRepository = redditApiRepository)
            }
        }
    }
}
