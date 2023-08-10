package com.example.lurkforreddit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lurkforreddit.model.Comment
import com.example.lurkforreddit.model.CommentThreadItem
import com.example.lurkforreddit.model.More
import com.example.lurkforreddit.model.Post

@Composable
fun CommentThread(
    post: Post,
    thread: List<CommentThreadItem>,
    openLink: (String) -> Unit,
    openProfile: (String) -> Unit,
    onMoreClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {

        item {
            CommentsHeader(
                post = post,
                openLink = openLink
            )
        }

        item {
            Text(text = "Thread Size: ${thread.size}", modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.titleMedium)
        }

        items(thread.size) { index ->
            thread[index].let { item ->
                when(item) {
                    is Comment ->
                        CommentCard(
                            postAuthor = post.author,
                            commentAuthor = item.author,
                            score = item.score,
                            createdUtc = item.createdUtc,
                            body = item.body,
                            numReplies = 0,
                            showReplies = true,
                            scoreHidden = item.scoreHidden,
                            depth = item.depth,
                            openProfile = openProfile
                        )
                    is More ->
                        MoreCommentsIndicator(
                            depth = item.depth,
                            numberOfComments = item.children.size,
                            onMoreClicked = { onMoreClicked(index) }
                        )

                }
            }
        }
    }
}