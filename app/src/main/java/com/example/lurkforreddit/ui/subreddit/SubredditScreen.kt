package com.example.lurkforreddit.ui.subreddit

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
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lurkforreddit.domain.model.ListingSort
import com.example.lurkforreddit.domain.model.Post
import com.example.lurkforreddit.domain.model.TopSort
import com.example.lurkforreddit.domain.util.NetworkResponse
import com.example.lurkforreddit.ui.common.PostList
import com.example.lurkforreddit.ui.common.PostListSortMenu
import com.example.lurkforreddit.ui.common.screens.ErrorScreen
import com.example.lurkforreddit.ui.common.screens.LoadingScreen
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubredditScreen(
    title: String,
    networkResponse: NetworkResponse<Flow<PagingData<Post>>>,
    selectedSort: ListingSort,
    onBackClicked: () -> Unit,
    onPostClicked: (Post) -> Unit,
    onProfileClicked: (String) -> Unit,
    onSubredditClicked: (String) -> Unit,
    onBrowserClicked: (String, String) -> Unit,
    onLinkClicked: (String) -> Unit,
    onListingSortChanged: (ListingSort, TopSort?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            SubredditTopBar(
                title = title,
                selectedSort = selectedSort,
                scrollBehavior = scrollBehavior,
                onBackClicked = { onBackClicked() },
                onListingSortChanged = onListingSortChanged
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->

        when (networkResponse) {
            is NetworkResponse.Loading -> LoadingScreen(modifier)
            is NetworkResponse.Success ->
                PostList(
                    posts = networkResponse.data.collectAsLazyPagingItems(),
                    onPostClicked = onPostClicked,
                    onProfileClicked = onProfileClicked,
                    onSubredditClicked = onSubredditClicked,
                    onBrowserClicked = onBrowserClicked,
                    openLink = onLinkClicked,
                    modifier = modifier.padding(paddingValues)
                )
            is NetworkResponse.Error -> ErrorScreen(modifier)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubredditTopBar(
    title: String,
    selectedSort: ListingSort,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClicked: () -> Unit,
    onListingSortChanged: (ListingSort, TopSort?) -> Unit,
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        title = {
            Text(
                text = title,
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
            PostListSortMenu(
                selectedSort = selectedSort,
                onListingSortChanged = onListingSortChanged
            )
        },
        scrollBehavior = scrollBehavior
    )
}