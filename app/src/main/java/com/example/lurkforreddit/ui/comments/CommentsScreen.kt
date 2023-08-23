package com.example.lurkforreddit.ui.comments

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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import com.example.lurkforreddit.R
import com.example.lurkforreddit.ui.comments.components.CommentThread
import com.example.lurkforreddit.ui.comments.components.CommentSortMenu
import com.example.lurkforreddit.domain.model.CommentSort
import com.example.lurkforreddit.ui.common.screens.ErrorScreen
import com.example.lurkforreddit.ui.common.screens.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsScreen(
    subreddit: String,
    selectedSort: CommentSort,
    networkResponse: CommentsNetworkResponse,
    onBackClicked: () -> Unit,
    onSortChanged: (CommentSort) -> Unit,
    onDuplicatesClicked: () -> Unit,
    onLinkClicked: (String) -> Unit,
    onProfileClicked: (String) -> Unit,
    onBrowserClicked: (String, String) -> Unit,
    onChangeVisibility: (Boolean, Int, Int) -> Unit,
    onMoreClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
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
                        onClick = { onBackClicked() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    CommentSortMenu(
                        selectedSort = selectedSort,
                        onSortChanged = onSortChanged
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
    ) { paddingValues ->

        when (networkResponse) {
            is CommentsNetworkResponse.Loading -> LoadingScreen(modifier)
            is CommentsNetworkResponse.Success -> {
                CommentThread(
                    postDto = networkResponse.postDto,
                    thread = networkResponse.commentThread,
                    openLink = onLinkClicked,
                    openProfile = onProfileClicked,
                    onBrowserClicked = onBrowserClicked,
                    onMoreClicked = onMoreClicked,
                    onChangeVisibility = onChangeVisibility,
                    modifier = modifier.padding(paddingValues)
                )
            }
            is CommentsNetworkResponse.Error -> ErrorScreen(modifier)
        }
    }
}