package com.example.lurkforreddit.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lurkforreddit.R
import com.example.lurkforreddit.network.model.CommentApi
import com.example.lurkforreddit.network.model.MoreApi
import com.example.lurkforreddit.network.model.PostApi
import com.example.lurkforreddit.ui.components.CommentCard
import com.example.lurkforreddit.ui.components.CommentSortMenu
import com.example.lurkforreddit.ui.components.PostThumbnail
import com.example.lurkforreddit.util.CommentSort

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsScreen(
    uiState: State<CommentsUiState>,
    subreddit: String,
    article: String,
    onDetailsBackClicked: () -> Unit,
    onSortChanged: (CommentSort) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                title = {
                    Text(
                        text = subreddit,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onDetailsBackClicked() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    CommentSortMenu(
                        selectedSort = uiState.value.commentSort,
                        onSortChanged = { sort ->
                            onSortChanged(sort)
                        }
                    )
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_other_discussions),
                            contentDescription = "Other Discussions"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        when (uiState.value.networkResponse) {
            is CommentsNetworkRequest.Loading -> LoadingScreen(modifier)
            is CommentsNetworkRequest.Success -> {
                PostComments(
                    post = (uiState.value.networkResponse as CommentsNetworkRequest.Success).postData.first,
                    commentTree = (uiState.value.networkResponse as CommentsNetworkRequest.Success).postData.second,
                    modifier = modifier.padding(paddingValues = it)
                )
            }

            is CommentsNetworkRequest.Error -> ErrorScreen(modifier)
        }
    }
}

@Composable
fun PostComments(
    post: PostApi,
    commentTree: Pair<List<CommentApi>, MoreApi?>,
    modifier: Modifier = Modifier
) {
    val comments = commentTree.first
    val more = commentTree.second

    LazyColumn(
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {

        item {
            CommentsHeader(
                post = post
            )
        }

        items(comments.size) { index ->
            comments[index].let { comment ->
                if (comment.contents != null) {
                    CommentCard(
                        contents = comment.contents,
                        replies = comment.replies.toMutableList(),
                        more = comment.more,
                        padding = 4,
                        color = 0,
                    )
                }
            }
        }
    }
}

@Composable
fun CommentsHeader(
    post: PostApi,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 16.dp)
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            if (!post.is_self) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(6.dp))
                ) {
                    PostThumbnail(
                        preview = post.preview,
                        thumbnail = post.thumbnail,
                        nsfw = post.over18,
                        url = post.url
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = post.title,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )

                Row() {
                    Text(
                        text = post.author,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleSmall
                    )

                    // TODO: Relative timestamp
                }


                if (post.is_self) {
                    Text(
                        text = post.selftext,
                        modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (post.over18) {
                        Text(
                            text = "nsfw",
                            color = Color.Red,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    if (post.locked) {
                        Image(
                            alignment = Alignment.Center,
                            painter = painterResource(R.drawable.ic_post_locked),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            colorFilter = ColorFilter.tint(Color.Yellow),
                            modifier = Modifier
                                .width(15.dp)
                                .height(15.dp)
                        )
                    }
                }
            }
        }
    }
}