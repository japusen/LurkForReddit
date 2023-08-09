package com.example.lurkforreddit.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import kotlinx.datetime.DateTimePeriod

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommentCard(
    postAuthor: String,
    contents: CommentContents,
    replies: List<Comment>,
    more: More?,
    padding: Int,
    color: Int,
    openProfile: (String) -> Unit,
    onNestedMoreClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showReplies by rememberSaveable { mutableStateOf(true) }
    val nestedPadding = 12

    Column(
        modifier = modifier
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
                        strokeWidth = 3.0F,
                        alpha = 0.75F
                    )
                }
            }
    ) {
        Divider()

        CommentContents(
            postAuthor = postAuthor,
            contents = contents,
            showReplies = showReplies,
            numReplies = if (more != null) replies.size + 1 else replies.size,
            openProfile = openProfile,
            modifier = modifier
                .combinedClickable(
                    enabled = true,
                    onLongClick = { showReplies = !showReplies },
                    onClick = {  }
                )
        )

        if (showReplies) {
            if (replies.isNotEmpty()) {
                replies.forEach { reply ->
                    reply.contents?.let {
                        CommentCard(
                            postAuthor = postAuthor,
                            contents = it,
                            replies = reply.replies.toMutableList(),
                            more = reply.more,
                            padding = nestedPadding,
                            color = color + 1,
                            openProfile = openProfile,
                            onNestedMoreClicked = onNestedMoreClicked
                        )
                    }
                }
            }

            if (more != null) {
                MoreCommentsIndicator(
                    padding = nestedPadding,
                    color = color + 1,
                    numberOfComments = more.children.size,
                    onMoreClicked = { onNestedMoreClicked(contents.id) }
                )
            }
        }
    }
}

@Composable
private fun CommentContents(
    postAuthor: String,
    contents: CommentContents,
    showReplies: Boolean,
    numReplies: Int,
    openProfile: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(8.dp)
    ) {

        AuthorDetails(
            postAuthor = postAuthor,
            commentAuthor = contents.author,
            isScoreHidden = contents.scoreHidden,
            score = contents.score,
            publishedTime = relativeTime(contents.createdUtc),
            openProfile = openProfile,
            showReplies = showReplies,
            numReplies = numReplies
        )

        CommentBody(body = contents.body)
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AuthorDetails(
    postAuthor: String,
    commentAuthor: String,
    isScoreHidden: Boolean,
    score: Int,
    publishedTime: DateTimePeriod,
    openProfile: (String) -> Unit,
    showReplies: Boolean,
    numReplies: Int,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val authorModifier =
            if (postAuthor == commentAuthor)
                Modifier.background(MaterialTheme.colorScheme.inversePrimary, RoundedCornerShape(4.dp))
            else
                Modifier
        Text(
            text = commentAuthor,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            modifier = authorModifier.clickable { openProfile(commentAuthor) }
        )

        Text(
            text = if (isScoreHidden) "[score hidden]" else "$score points",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleMedium

        )
        TimeStamp(
            time = publishedTime,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        if ((!showReplies && numReplies > 0)) {
            Badge(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(top = 0.dp)
            ) {
                Text(
                    text = "+${numReplies}",
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

@Composable
fun CommentBody(
    body: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = body,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.padding(8.dp)
    )
}