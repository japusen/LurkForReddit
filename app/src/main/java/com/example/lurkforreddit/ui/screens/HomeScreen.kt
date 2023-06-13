package com.example.lurkforreddit.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.lurkforreddit.network.model.Content
import com.example.lurkforreddit.network.model.PostApi
import com.example.lurkforreddit.network.model.ProfileCommentApi

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

@Composable
fun ResultScreen(submissions: LazyPagingItems<Content>, modifier: Modifier = Modifier) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(submissions.itemCount) { index ->
            submissions[index]?.let { content ->
                when (content) {
                    is PostApi -> {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text((index + 1).toString())
                                Column() {
                                    Text(content.title)
                                    Text("${content.author} - ${content.subreddit}")
                                    Text("Self: ${content.is_self}")
                                    Text("Gallery: ${content.is_gallery}")
                                    Text("Type: ${content.postType}")
                                    Text("Url: ${content.url}")
                                }
                            }
                        }
                    }

                    is ProfileCommentApi -> {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(index.toString())
                                Column() {
                                    Text(content.body)
                                    Text("${content.author} - ${content.subreddit}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}