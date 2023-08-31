package com.example.lurkforreddit.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lurkforreddit.R
import com.example.lurkforreddit.domain.model.ListingSort
import com.example.lurkforreddit.domain.model.Post
import com.example.lurkforreddit.domain.model.SearchResult
import com.example.lurkforreddit.domain.model.TopSort
import com.example.lurkforreddit.domain.util.NetworkResponse
import com.example.lurkforreddit.ui.common.PostCard
import com.example.lurkforreddit.ui.common.PostList
import com.example.lurkforreddit.ui.common.PostListSortMenu
import com.example.lurkforreddit.ui.common.screens.ErrorScreen
import com.example.lurkforreddit.ui.common.screens.LoadingScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    query: String,
    subreddit: String,
    selectedSort: ListingSort,
    networkResponse: NetworkResponse<Flow<PagingData<Post>>>,
    searchResult: List<SearchResult>,
    postHistory: List<Post>,
    isShowingPostHistory: Boolean,
    updateSearchResults: () -> Unit,
    setQuery: (String) -> Unit,
    clearQuery: () -> Unit,
    onListingSortChanged: (ListingSort, TopSort?) -> Unit,
    onPostClicked: (Post) -> Unit,
    onProfileClicked: (String) -> Unit,
    onSubredditClicked: (String) -> Unit,
    onBrowserClicked: (String, String) -> Unit,
    onLinkClicked: (String) -> Unit,
    onShowPostHistory: (Boolean) -> Unit,
    onClearPostHistory: () -> Unit,
    onGetPostHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            SearchMenu(
                query = query,
                searchResult = searchResult,
                setQuery = setQuery,
                updateSearchResults = updateSearchResults,
                clearQuery = clearQuery,
                onProfileClicked = onProfileClicked,
                onSubredditClicked = onSubredditClicked,
                closeDrawer = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                },
                onShowPostHistory = onShowPostHistory,
                onGetPostHistory = onGetPostHistory,
            )
        },
        drawerState = drawerState
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        Scaffold(
            topBar = {
                HomeTopBar(
                    subreddit = subreddit,
                    selectedSort = selectedSort,
                    scrollBehavior = scrollBehavior,
                    onDrawerOpen = { coroutineScope.launch { drawerState.open() } },
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

            AnimatedVisibility(isShowingPostHistory) {
                ModalBottomSheet(
                    onDismissRequest = { onShowPostHistory(false) },
                    modifier = Modifier.fillMaxSize()
                ) {

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.recently_viewed_posts),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(start = 16.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = {
                                onClearPostHistory()

                                onShowPostHistory(false)
                            },
                            modifier = Modifier
                                .padding(end = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "clear history",
                                modifier = Modifier
                                    .scale(0.8f)
                            )
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    LazyColumn(
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = modifier
                            .fillMaxWidth()
                    ) {
                        items(postHistory.size) { index ->
                            postHistory[index].let { post ->
                                PostCard(
                                    post = post,
                                    onPostClicked = { onPostClicked(post) },
                                    onProfileClicked = onProfileClicked,
                                    onSubredditClicked = onSubredditClicked,
                                    onBrowserClicked = onBrowserClicked,
                                    openLink = onLinkClicked
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    subreddit: String,
    selectedSort: ListingSort,
    scrollBehavior: TopAppBarScrollBehavior,
    onDrawerOpen: () -> Unit,
    onListingSortChanged: (ListingSort, TopSort?) -> Unit,
) {

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        title = {
            Text(
                text = subreddit,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { onDrawerOpen() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = stringResource(R.string.back)
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