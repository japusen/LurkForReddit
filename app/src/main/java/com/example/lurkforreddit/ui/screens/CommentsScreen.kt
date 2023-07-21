package com.example.lurkforreddit.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.example.lurkforreddit.ui.components.CommentsList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsScreen(
    title: @Composable () -> Unit,
    navIcon: @Composable () -> Unit,
    actions: @Composable () -> Unit,
    networkResponse: CommentsNetworkResponse,
    openLink: (String) -> Unit,
    openProfile: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                title = title,
                navigationIcon = navIcon,
                actions = { actions() },
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
                    openLink = openLink,
                    openProfile = openProfile,
                    modifier = modifier.padding(paddingValues)
                )
            }
            is CommentsNetworkResponse.Error -> ErrorScreen(modifier)
        }
    }
}