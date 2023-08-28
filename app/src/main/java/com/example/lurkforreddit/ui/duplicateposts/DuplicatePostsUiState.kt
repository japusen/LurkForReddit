package com.example.lurkforreddit.ui.duplicateposts

import androidx.paging.PagingData
import com.example.lurkforreddit.domain.model.Content
import com.example.lurkforreddit.domain.model.DuplicatesSort
import com.example.lurkforreddit.domain.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

data class DuplicatesUiState(
    val networkResponse: NetworkResponse<Flow<PagingData<Content>>> = NetworkResponse.Loading,
    val sort: DuplicatesSort = DuplicatesSort.NUMCOMMENTS,
)