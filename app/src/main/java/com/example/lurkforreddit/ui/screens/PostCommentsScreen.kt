package com.example.lurkforreddit.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.lurkforreddit.network.model.CommentApi
import com.example.lurkforreddit.network.model.MoreApi
import com.example.lurkforreddit.network.model.PostApi

@Composable
fun PostDetailsScreen(
    postDetailsViewModel: PostDetailsViewModel,
    subreddit: String,
    article: String,
    onDetailsBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        postDetailsViewModel.loadPostComments(subreddit, article)
    }

    when (val detailsState = postDetailsViewModel.detailsState) {
        is DetailsState.Loading -> LoadingScreen(modifier)
        is DetailsState.Success ->
            PostDetails(
                postApi = detailsState.postData.first,
                commentTree = detailsState.postData.second,
                onDetailsBackClicked = onDetailsBackClicked
            )

        is DetailsState.Error -> ErrorScreen(modifier)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetails(
    postApi: PostApi,
    commentTree: Pair<List<CommentApi>, MoreApi?>,
    onDetailsBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(postApi.subreddit) },
                navigationIcon = {
                    IconButton(
                        onClick = { onDetailsBackClicked() }
                    ) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) {
        Card(
            modifier = modifier.padding(it)
        ) {
            Text(postApi.title)
        }
    }
}