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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import com.example.lurkforreddit.R
import com.example.lurkforreddit.ui.components.CommentsList
import com.example.lurkforreddit.ui.components.menus.CommentSortMenu
import com.example.lurkforreddit.model.CommentSort

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
    onMoreClicked: () -> Unit,
    onNestedMoreClicked: (String) -> Unit,
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
                CommentsList(
                    post = networkResponse.post,
                    comments = networkResponse.comments,
                    more = networkResponse.more,
                    openLink = onLinkClicked,
                    openProfile = onProfileClicked,
                    onMoreClicked = onMoreClicked,
                    onNestedMoreClicked = onNestedMoreClicked,
                    modifier = modifier.padding(paddingValues)
                )
            }
            is CommentsNetworkResponse.Error -> ErrorScreen(modifier)
        }
    }
}