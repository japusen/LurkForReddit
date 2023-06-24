package com.example.lurkforreddit.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lurkforreddit.R
import com.example.lurkforreddit.network.model.CommentApi
import com.example.lurkforreddit.network.model.CommentContents
import com.example.lurkforreddit.network.model.MoreApi
import com.example.lurkforreddit.network.model.PostApi
import com.example.lurkforreddit.ui.components.CommentCard
import com.example.lurkforreddit.ui.components.CommentSortMenu
import com.example.lurkforreddit.util.CommentSort

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailsScreen(
    detailsState: DetailsState,
    subreddit: String,
    article: String,
    onDetailsBackClicked: () -> Unit,
    onSortChanged: (CommentSort) -> Unit,
    modifier: Modifier = Modifier,
) {
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
                            contentDescription = "back"
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
                }

            )
        }
    ) {
        when (detailsState) {
            is DetailsState.Loading -> LoadingScreen(modifier)
            is DetailsState.Success ->
                PostDetails(
                    postApi = detailsState.postData.first,
                    commentTree = detailsState.postData.second,
                    modifier = modifier.padding(paddingValues = it)
                )

            is DetailsState.Error -> ErrorScreen(modifier)
        }
    }
}

@Composable
fun PostDetails(
    postApi: PostApi,
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