package com.example.lurkforreddit.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lurkforreddit.model.Comment
import com.example.lurkforreddit.model.CommentContents
import com.example.lurkforreddit.model.More
import com.example.lurkforreddit.util.relativeTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommentCard(
    postAuthor: String,
    contents: CommentContents,
    replies: MutableList<Comment>,
    more: More?,
    padding: Int,
    color: Int,
    modifier: Modifier = Modifier
) {
    var showReplies by rememberSaveable { mutableStateOf(true) }

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
                        strokeWidth = 2F,
                        alpha = 0.5F
                    )
                }
            }
    ) {
        Divider()

        CommentContents(
            postAuthor = postAuthor,
            contents = contents,
            modifier = Modifier
                .combinedClickable(
                    enabled = true,
                    onLongClick = { showReplies = !showReplies },
                    onClick = {  }
                )
        )

        if (showReplies) {
            replies.forEach { reply ->
                if (replies.isNotEmpty()) {
                    reply.contents?.let {
                        CommentCard(
                            postAuthor = postAuthor,
                            contents = it,
                            replies = reply.replies.toMutableList(),
                            more = reply.more,
                            padding = padding + 2,
                            color = color + 1
                        )
                    }
                }
            }
        } else if (replies.isNotEmpty()) {
            Text(
                text = if (replies.size == 1 ) "${replies.size} reply hidden" else "${replies.size} replies hidden",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(8.dp)
            )
        }

        if (more != null) {
            Text(
                text = "more comments (${more.count})",
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(start = (padding + 4).dp, top = 4.dp, bottom = 4.dp)
                    .clickable { /* TODO load more comments */ }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CommentContents(
    postAuthor: String,
    contents: CommentContents,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(8.dp)
    ) {

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = contents.author,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                modifier =
                if (postAuthor == contents.author)
                    Modifier.background(MaterialTheme.colorScheme.inversePrimary, RoundedCornerShape(4.dp))
                else Modifier
            )

            Text(
                text = if (contents.scoreHidden) "[score hidden]" else "${contents.score} points",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium

            )
            TimeStamp(
                time = relativeTime(contents.createdUtc),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium
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