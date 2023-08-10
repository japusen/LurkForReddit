package com.example.lurkforreddit.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lurkforreddit.util.relativeTime
import kotlinx.datetime.DateTimePeriod

@Composable
fun CommentCard(
    postAuthor: String,
    commentAuthor: String,
    depth: Int,
    score: Int,
    createdUtc: Float,
    body: String,
    numReplies: Int,
    showReplies: Boolean,
    scoreHidden: Boolean,
    openProfile: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = (depth * 6).dp)
            .drawBehind {
                if (depth != 0) {
                    drawLine(
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = 0f, y = size.height),
                        color = when (depth % 4) {
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
        Divider(modifier = Modifier.fillMaxWidth())
        AuthorDetails(
            postAuthor = postAuthor,
            commentAuthor = commentAuthor,
            isScoreHidden = scoreHidden,
            score = score,
            publishedTime = relativeTime(createdUtc),
            openProfile = openProfile,
            showReplies = showReplies,
            numReplies = numReplies
        )

        CommentBody(body = body)
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
    openProfile: (String) -> Unit,
    showReplies: Boolean,
    numReplies: Int,
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

        AnimatedVisibility ((!showReplies && numReplies > 0)) {
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