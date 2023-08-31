package com.example.lurkforreddit.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.lurkforreddit.domain.model.Content
import com.example.lurkforreddit.domain.model.Post
import com.example.lurkforreddit.domain.model.ProfileComment
import com.example.lurkforreddit.ui.common.PostCard

@Composable
fun ProfileContentList(
    contentList: LazyPagingItems<Content>,
    onPostClicked: (Post) -> Unit,
    onCommentClicked: (String, String) -> Unit,
    onProfileClicked: (String) -> Unit,
    onSubredditClicked: (String) -> Unit,
    onBrowserClicked: (String, String) -> Unit,
    openLink: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(contentList.itemCount) { index ->
            contentList[index]?.let { content ->
                when (content) {
                    is Post -> {
                        PostCard(
                            post = content,
                            onPostClicked = { onPostClicked(content) },
                            onProfileClicked = onProfileClicked,
                            onSubredditClicked = onSubredditClicked,
                            onBrowserClicked = onBrowserClicked,
                            openLink = openLink
                        )
                    }
                    is ProfileComment -> {
                        ProfileCommentCard(
                            comment = content,
                            onCommentClicked = { onCommentClicked(content.subreddit, content.linkID.substring(3)) },
                        )
                    }
                }
            }
        }
    }
}