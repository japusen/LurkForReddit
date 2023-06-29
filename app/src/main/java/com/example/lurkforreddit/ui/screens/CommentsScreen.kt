package com.example.lurkforreddit.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import com.example.lurkforreddit.R
import com.example.lurkforreddit.ui.components.CommentSortMenu
import com.example.lurkforreddit.ui.components.CommentsList
import com.example.lurkforreddit.util.CommentSort

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsScreen(
    uiState: State<CommentsUiState>,
    subreddit: String,
    onDetailsBackClicked: () -> Unit,
    onSortChanged: (CommentSort) -> Unit,
    onDuplicatesClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                title = {
                    Text(
                        text = subreddit,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onDetailsBackClicked() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    CommentSortMenu(
                        selectedSort = uiState.value.commentSort,
                        onSortChanged = { sort ->
                            onSortChanged(sort)
                        }
                    )
                    IconButton(
                        onClick = { onDuplicatesClicked() }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_other_discussions),
                            contentDescription = "Other Discussions"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        when (uiState.value.networkResponse) {
            is CommentsNetworkRequest.Loading -> LoadingScreen(modifier)
            is CommentsNetworkRequest.Success -> {
                CommentsList(
                    post = (uiState.value.networkResponse as CommentsNetworkRequest.Success).postData.first,
                    commentTree = (uiState.value.networkResponse as CommentsNetworkRequest.Success).postData.second,
                    modifier = modifier.padding(paddingValues = it)
                )
            }
            is CommentsNetworkRequest.Error -> ErrorScreen(modifier)
        }
    }
}