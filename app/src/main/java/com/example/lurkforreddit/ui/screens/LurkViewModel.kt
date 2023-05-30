package com.example.lurkforreddit.ui.screens

import android.util.Log
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
import com.example.lurkforreddit.model.CommentSort
import com.example.lurkforreddit.model.ListingSort
import com.example.lurkforreddit.model.ListingTopSort
import com.example.lurkforreddit.model.Thing
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed interface LurkUiState {
    data class Success(val text: Thing) : LurkUiState
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
            //getListing("all", ListingSort.RISING)
            //getTopListing("all", ListingTopSort.ALL)
            getComments("all", "13ve8iq", CommentSort.BEST)
        }
    }

//    fun getListing(subreddit: String, sort: ListingSort) {
//        viewModelScope.launch {
//            lurkUiState = try {
//                LurkUiState.Success(
//                    redditApiRepository.getListing(subreddit, sort)
//                )
//            } catch (e: IOException) {
//                LurkUiState.Error
//            } catch (e: HttpException) {
//                LurkUiState.Error
//            }
//        }
//    }
//
//    fun getTopListing(subreddit: String, sort: ListingTopSort) {
//        viewModelScope.launch {
//            lurkUiState = try {
//                LurkUiState.Success(
//                    redditApiRepository.getTopListing(subreddit, sort)
//                )
//            } catch (e: IOException) {
//                LurkUiState.Error
//            } catch (e: HttpException) {
//                LurkUiState.Error
//            }
//        }
//    }

    fun getComments(subreddit: String, article: String, sort: CommentSort) {
        viewModelScope.launch {
            lurkUiState = try {
                LurkUiState.Success(
                    redditApiRepository.getComments(subreddit, article, sort)
                )
            } catch (e: IOException) {
                Log.d("COMMENTS", e.toString())
                LurkUiState.Error
            } catch (e: HttpException) {
                Log.d("COMMENTS", e.toString())
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