package com.example.lurkforreddit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.lurkforreddit.network.model.Content
import com.example.lurkforreddit.network.model.PostApi
import com.example.lurkforreddit.network.model.ProfileCommentApi

@Composable
fun ListingFeed(
    submissions: LazyPagingItems<Content>,
    onPostClicked: (String, String) -> Unit,
    onProfileClicked: (String) -> Unit,
    onSubredditClicked: (String) -> Unit,
    onBrowserClicked: (String) -> Unit,
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
                            onPostClicked = { onPostClicked(content.subreddit, content.id) },
                            onProfileClicked = onProfileClicked,
                            onSubredditClicked = onSubredditClicked,
                            onBrowserClicked = onBrowserClicked
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