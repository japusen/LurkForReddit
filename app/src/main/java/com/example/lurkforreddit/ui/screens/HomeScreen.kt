package com.example.lurkforreddit.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lurkforreddit.R
import com.example.lurkforreddit.network.Content
import com.example.lurkforreddit.network.PostApi
import com.example.lurkforreddit.network.ProfileCommentApi

@Composable
fun HomeScreen(
    lurkUiState: LurkUiState,
    modifier: Modifier = Modifier
) {
    when (lurkUiState) {
        is LurkUiState.Loading -> LoadingScreen(modifier)
        is LurkUiState.Success -> ResultScreen(
            lurkUiState.feedContent.collectAsLazyPagingItems(),
            modifier
        )

        is LurkUiState.Error -> ErrorScreen(modifier)
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
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

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(stringResource(R.string.loading_failed))
    }
}

/**
 * The home screen displaying result of fetching photos.
 */
@Composable
fun ResultScreen(submissions: LazyPagingItems<Content>, modifier: Modifier = Modifier) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(submissions.itemCount) { index ->
            submissions[index]?.let {
                when (it) {
                    is PostApi -> {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column() {
                                Text(text = it.title)
                                Text("${it.author} - ${it.subreddit}")
                            }
                        }
                    }

                    is ProfileCommentApi -> {
                        Card() {
                            Column() {
                                Text(text = it.body)
                                Text("${it.author} - ${it.subreddit}")
                            }
                        }
                    }
                }
            }
        }
    }
}