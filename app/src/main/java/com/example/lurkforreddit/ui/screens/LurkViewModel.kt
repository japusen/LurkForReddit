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
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed interface LurkUiState {
    data class Success(val text: String) : LurkUiState
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
            getSubredditListing("all")
        }
    }

    fun getSubredditListing(subreddit: String) {
        viewModelScope.launch {
            lurkUiState = try {
                LurkUiState.Success(redditApiRepository.getSubredditListing(subreddit))
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