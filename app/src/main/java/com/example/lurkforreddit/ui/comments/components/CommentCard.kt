package com.example.lurkforreddit.ui.comments.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lurkforreddit.R
import com.example.lurkforreddit.ui.common.ActionButton
import com.example.lurkforreddit.ui.common.TimeStamp
import com.example.lurkforreddit.util.relativeTime
import kotlinx.datetime.DateTimePeriod

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommentCard(
    postAuthor: String,
    commentAuthor: String,
    depth: Int,
    score: Int,
    createdUtc: Float,
    body: String,
    permalink: String,
    scoreHidden: Boolean,
    onChangeVisibility: (Boolean) -> Unit,
    openProfile: (String) -> Unit,
    onBrowserClicked: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var showReplies by remember { mutableStateOf(true) }
    Column(
        modifier = modifier
            .combinedClickable(
                enabled = true,
                onLongClick = {
                    showReplies = !showReplies
                    onChangeVisibility(showReplies)
                },
                onClick = { expanded = !expanded }
            )
            .fillMaxWidth()
            .padding(start = (depth * 8).dp, top = 2.dp, bottom = 2.dp)
            .drawBehind {
                if (depth != 0) {
                    drawLine(
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = 0f, y = size.height),
                        color = when (depth % 7) {
                            0 -> Color.Magenta
                            1 -> Color(75, 0, 130)
                            2 -> Color.Blue
                            3 -> Color.Green
                            4 -> Color.Yellow
                            5 -> Color(255, 127, 0)
                            6 -> Color.Red
                            else -> Color(148, 0, 211)
                        },
                        strokeWidth = 3.0F,
                        alpha = 0.75F
                    )
                }
            }
    ) {
        Divider(modifier = Modifier.fillMaxWidth())

        AuthorDetails(
            postAuthor = postAuthor,
            commentAuthor = commentAuthor,
            isScoreHidden = scoreHidden,
            score = score,
            publishedTime = relativeTime(createdUtc),
            showReplies = showReplies,
        )

        CommentBody(body = body)

        AnimatedVisibility(visible = expanded) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                CommentActions(
                    onProfileClicked = { openProfile(commentAuthor) },
                    onBrowserClicked = {
                        onBrowserClicked(
                            "www.reddit.com$permalink",
                            "reddit.com"
                        )
                    },
                    modifier = Modifier.weight(1F)
                )
            }
        }

        Divider(modifier = Modifier.fillMaxWidth())
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
    showReplies: Boolean,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
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
            modifier = authorModifier
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

        AnimatedVisibility (!showReplies) {
            Badge(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(top = 0.dp)
            ) {
                Text(
                    text = stringResource(R.string.replies_hidden),
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

@Composable
fun CommentActions(
    onProfileClicked: () -> Unit,
    onBrowserClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sizeModifier = modifier
        .height(20.dp)
        .width(20.dp)

    ActionButton(
        onAction = { onProfileClicked() },
        iconID = R.drawable.ic_profile,
        description = "open profile",
        modifier = sizeModifier
    )
    ActionButton(
        onAction = { onBrowserClicked() },
        iconID = R.drawable.ic_open_in_browser,
        description = "open comment in browser",
        modifier = sizeModifier
    )
}