package com.example.lurkforreddit.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lurkforreddit.R
import com.example.lurkforreddit.domain.model.Content
import com.example.lurkforreddit.domain.model.ListingSort
import com.example.lurkforreddit.domain.model.Post
import com.example.lurkforreddit.domain.util.NetworkResponse
import com.example.lurkforreddit.domain.model.SearchResult
import com.example.lurkforreddit.domain.model.TopSort
import com.example.lurkforreddit.ui.common.ListingFeed
import com.example.lurkforreddit.ui.common.ListingSortMenu
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
    networkResponse: NetworkResponse<Flow<PagingData<Content>>>,
    searchResult: List<SearchResult>,
    updateSearchResults: () -> Unit,
    setQuery: (String) -> Unit,
    clearQuery: () -> Unit,
    onListingSortChanged: (ListingSort, TopSort?) -> Unit,
    onPostClicked: (Post) -> Unit,
    onCommentClicked: (String, String) -> Unit,
    onProfileClicked: (String) -> Unit,
    onSubredditClicked: (String) -> Unit,
    onBrowserClicked: (String, String) -> Unit,
    onLinkClicked: (String) -> Unit,
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
                }
            )
        },
        drawerState = drawerState
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
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { coroutineScope.launch { drawerState.open() } }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Menu,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    },
                    actions = {
                        ListingSortMenu(
                            selectedSort = selectedSort,
                            onListingSortChanged = onListingSortChanged
                        )
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { paddingValues ->

            when (networkResponse) {
                is NetworkResponse.Loading -> LoadingScreen(modifier)
                is NetworkResponse.Success ->
                    ListingFeed(
                        submissions = networkResponse.data.collectAsLazyPagingItems(),
                        onPostClicked = onPostClicked,
                        onCommentClicked = onCommentClicked,
                        onProfileClicked = onProfileClicked,
                        onSubredditClicked = onSubredditClicked,
                        onBrowserClicked = onBrowserClicked,
                        openLink = onLinkClicked,
                        modifier = modifier.padding(paddingValues)
                    )
                is NetworkResponse.Error -> ErrorScreen(modifier)
            }

//            ModalBottomSheet(onDismissRequest = { /*TODO*/ }) {
//
//            }
        }
    }
}