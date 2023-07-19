package com.example.lurkforreddit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lurkforreddit.model.Comment
import com.example.lurkforreddit.model.More
import com.example.lurkforreddit.model.Post

@Composable
fun CommentsList(
    post: Post,
    commentTree: Pair<List<Comment>, More?>,
    openLink: (String) -> Unit,
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
                post = post,
                openLink = openLink
            )
        }

        items(comments.size) { index ->
            comments[index].let { comment ->
                if (comment.contents != null) {
                    CommentCard(
                        postAuthor = post.author,
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