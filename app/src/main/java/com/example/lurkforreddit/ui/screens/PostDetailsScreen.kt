package com.example.lurkforreddit.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.lurkforreddit.R
import com.example.lurkforreddit.network.model.CommentApi
import com.example.lurkforreddit.network.model.CommentContents
import com.example.lurkforreddit.network.model.MoreApi
import com.example.lurkforreddit.network.model.PostApi
import com.example.lurkforreddit.network.model.ProfileCommentApi
import com.example.lurkforreddit.ui.components.CommentSortMenu
import com.example.lurkforreddit.ui.components.PostCard
import com.example.lurkforreddit.ui.components.ProfileCommentCard
import com.example.lurkforreddit.util.CommentSort
import kotlinx.serialization.json.JsonNull.content
import org.w3c.dom.Comment

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
                title = { Text(subreddit) },
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
                        onClick = {  }
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
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(comments.size) { index ->
            comments[index].let { comment ->
                if (comment.contents != null) {
                    CommentCards(
                        contents = comment.contents,
                        replies = comment.replies,
                        more = comment.more,
                        padding = 0,
                        color = 0,
                        modifier = modifier
                    )
                }
            }
        }
    }
}

@Composable
fun CommentCards(
    contents: CommentContents,
    replies: List<CommentApi>,
    more: MoreApi?,
    padding: Int,
    color: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = padding.dp)
            .drawBehind {
                if (color != 0) {
                    drawLine(
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = 0f, y = size.height),
                        color = when (color % 4) {
                            0 -> Color.Magenta
                            1 -> Color.Blue
                            2 -> Color.Green
                            else -> Color.Red
                        },
                        strokeWidth = 2F
                    )
                }
            }
    ) {

        CommentContents(contents = contents)

        replies.forEach { reply ->
            if (replies.isNotEmpty()) {
                reply.contents?.let {
                    CommentCards(
                        contents = it,
                        replies = reply.replies,
                        more = reply.more,
                        padding = padding + 4,
                        color = color + 1
                    )
                }
            }
        }

        if (more != null) {
            Text(
                text = "more comments (${more.count})",
                color = MaterialTheme.colorScheme.inversePrimary,
                modifier = Modifier
                    .padding(start = padding.dp, top = 4.dp, bottom = 4.dp)
            )
        }
    }
}

@Composable
private fun CommentContents(
    contents: CommentContents
) {
    Column(
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = contents.author, color = MaterialTheme.colorScheme.primary)
            Text(
                text = "${contents.score} points",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = contents.body,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(8.dp)
        )
    }
}