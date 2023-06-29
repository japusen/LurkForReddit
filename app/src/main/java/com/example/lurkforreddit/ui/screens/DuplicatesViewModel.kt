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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed interface DuplicatesNetworkRequest {
    data class Success(
        val listingContent: Flow<PagingData<Content>>,
    ) : DuplicatesNetworkRequest

    object Error : DuplicatesNetworkRequest
    object Loading : DuplicatesNetworkRequest
}

data class DuplicatesUiState(
    val networkResponse: DuplicatesNetworkRequest = DuplicatesNetworkRequest.Loading,
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
                        DuplicatesNetworkRequest.Success(
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
                        DuplicatesNetworkRequest.Error
                    } catch (e: HttpException) {
                        DuplicatesNetworkRequest.Error
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
