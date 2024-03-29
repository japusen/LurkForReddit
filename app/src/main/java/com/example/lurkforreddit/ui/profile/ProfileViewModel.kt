package com.example.lurkforreddit.ui.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.lurkforreddit.domain.util.NetworkResponse
import com.example.lurkforreddit.domain.model.TopSort
import com.example.lurkforreddit.domain.model.UserContentType
import com.example.lurkforreddit.domain.model.UserListingSort
import com.example.lurkforreddit.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    val username: String = savedStateHandle["username"] ?: ""

    init {
        getUserContent()
    }

    private fun getUserContent() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    networkResponse = try {
                        NetworkResponse.Success(
                            if (currentState.contentType == UserContentType.SUBMITTED) {
                                profileRepository.getUserSubmissions(
                                    username = username,
                                    sort = currentState.userListingSort,
                                    topSort = currentState.topSort
                                ).cachedIn(viewModelScope)
                            } else {
                                profileRepository.getUserComments(
                                    username = username,
                                    sort = currentState.userListingSort,
                                    topSort = currentState.topSort
                                ).cachedIn(viewModelScope)
                            }
                        )
                    } catch (e: IOException) {
                        NetworkResponse.Error("Could not make network request")
                    } catch (e: HttpException) {
                        NetworkResponse.Error("Request Failed")
                    }
                )
            }
        }
    }

    /**
     * Change the content type and load the content
     * @param contentType the type of profile content to load (submissions or comments)
     * **/
    fun setContentType(contentType: UserContentType) {
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
    fun setListingSort(sort: UserListingSort, topSort: TopSort? = null) {
        _uiState.update { currentState ->
            currentState.copy(
                userListingSort = sort,
                topSort = topSort,
            )
        }
        getUserContent()
    }

//    companion object {
//        val Factory: ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val application = (this[APPLICATION_KEY] as LurkApplication)
//                val profileContentRepository = application.container.profileRepository
//                val savedStateHandle = createSavedStateHandle()
//                ProfileViewModel(
//                    profileRepository = profileContentRepository,
//                    savedStateHandle = savedStateHandle
//                )
//            }
//        }
//    }
}
