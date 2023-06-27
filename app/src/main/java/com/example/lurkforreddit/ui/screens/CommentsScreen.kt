package com.example.lurkforreddit.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lurkforreddit.R
import com.example.lurkforreddit.network.model.CommentApi
import com.example.lurkforreddit.network.model.MoreApi
import com.example.lurkforreddit.network.model.PostApi
import com.example.lurkforreddit.ui.components.CommentCard
import com.example.lurkforreddit.ui.components.CommentSortMenu
import com.example.lurkforreddit.ui.components.PostThumbnail
import com.example.lurkforreddit.util.CommentSort
import kotlinx.serialization.json.JsonNull.content
import java.time.format.TextStyle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsScreen(
    detailsState: DetailsState,
    subreddit: String,
    article: String,
    onDetailsBackClicked: () -> Unit,
    onSortChanged: (CommentSort) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = {
            TopAppBar(
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
        when (detailsState) {
            is DetailsState.Loading -> LoadingScreen(modifier)
            is DetailsState.Success -> {
                PostComments(
                    post = detailsState.postData.first,
                    commentTree = detailsState.postData.second,
                    modifier = modifier.padding(paddingValues = it)
                )
            }
            is DetailsState.Error -> ErrorScreen(modifier)
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
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = post.title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )

            if (!post.is_self) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
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
        }
    }
}