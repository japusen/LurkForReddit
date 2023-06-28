package com.example.lurkforreddit.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lurkforreddit.R
import com.example.lurkforreddit.network.model.Content
import com.example.lurkforreddit.network.model.PostApi
import com.example.lurkforreddit.network.model.ProfileCommentApi
import com.example.lurkforreddit.ui.components.PostCard
import com.example.lurkforreddit.ui.components.ProfileCommentCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: State<HomeUiState>,
    onPostClicked: (String?, String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                title = { Text("Lurk for Reddit") },
                actions = {

                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        when (uiState.value.networkResponse) {
            is ListingNetworkRequest.Loading -> LoadingScreen(modifier)
            is ListingNetworkRequest.Success ->
                ListingFeed(
                    (uiState.value.networkResponse as ListingNetworkRequest.Success).listingContent.collectAsLazyPagingItems(),
                    onPostClicked = onPostClicked,
                    modifier = modifier.padding(paddingValues)
                )
            is ListingNetworkRequest.Error -> ErrorScreen(modifier)
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Surface {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.drawable.loading_img),
                contentDescription = stringResource(R.string.loading),
                modifier = Modifier.size(200.dp)
            )
        }
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Surface {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            Text(stringResource(R.string.loading_failed))
        }
    }
}

@Composable
fun ListingFeed(
    submissions: LazyPagingItems<Content>,
    onPostClicked: (String?, String?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(submissions.itemCount) { index ->
            submissions[index]?.let { content ->
                when (content) {
                    is PostApi -> {
                        PostCard(
                            content = content,
                            onPostClicked = { onPostClicked(content.subreddit, content.id) }
                        )
                    }
                    is ProfileCommentApi -> {
                        ProfileCommentCard(content = content)
                    }
                }
            }
        }
    }
}