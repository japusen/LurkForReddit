package com.example.lurkforreddit.ui.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.lurkforreddit.R
import com.example.lurkforreddit.data.remote.model.PostDto
import com.example.lurkforreddit.util.relativeTime
import kotlinx.datetime.DateTimePeriod


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostCard(
    content: PostDto,
    onPostClicked: () -> Unit,
    onProfileClicked: (String) -> Unit,
    onSubredditClicked: (String) -> Unit,
    onBrowserClicked: (String, String) -> Unit,
    openLink: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onPostClicked() },
                onLongClick = { expanded = !expanded }
            )
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                if (!content.isSelfPost) {
                    PostThumbnail(
                        thumbnail = content.thumbnail,
                        url = content.url,
                        openLink = openLink,
                        modifier = modifier
                            .width(75.dp)
                            .height(75.dp)
                            .clip(RoundedCornerShape(6.dp))
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = content.title,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        SubredditAndAuthor(
                            subreddit = content.subreddit,
                            author = content.author,
                            distinguished = content.distinguished,
                            domain = content.domain,
                            isSelfPost = content.isSelfPost,
                            isGalleryPost = content.isGalleryPost
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                    ) {
                        ExtraDetails(
                            nsfw = content.over18,
                            locked = content.locked,
                            score = content.score,
                            numComments = content.numComments,
                            publishedTime = relativeTime(content.createdUtc)
                        )

                        Spacer(modifier.weight(1F))
                        ExpandButton(
                            expanded = expanded,
                            onExpand = { expanded = !expanded }
                        )
                    }
                }
            }
            AnimatedVisibility(visible = expanded) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    PostActions(
                        onProfileClicked = { onProfileClicked(content.author) },
                        onSubredditClicked = { onSubredditClicked(content.subreddit) },
                        onBrowserClicked = { onBrowserClicked(content.url, content.domain ?: "") },
                        modifier = Modifier.weight(1F)
                    )
                }
            }
        }
    }
}

@Composable
fun SubredditAndAuthor(
    subreddit: String,
    author: String,
    distinguished: String?,
    domain: String?,
    isSelfPost: Boolean,
    isGalleryPost: Boolean,
) {
    Text(
        text = subreddit,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.labelMedium
    )
    Text(
        text = author,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.labelMedium
    )
    if (distinguished != null && distinguished != "special") {
        val text: String
        val color: Color
        if (distinguished == "Moderator") {
            text = "[M]"
            color = Color.Green
        } else {
            text = "[A]"
            color = Color.Red
        }

        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelMedium
        )
    }
    if (domain != null && !isSelfPost && !isGalleryPost)
        Text(
            text = domain,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
}

@Composable
fun ExtraDetails(
    nsfw: Boolean,
    locked: Boolean,
    score: Int,
    numComments: Int,
    publishedTime: DateTimePeriod,
) {
    if (nsfw) {
        Text(
            text = "nsfw",
            color = Color.Red,
            style = MaterialTheme.typography.labelMedium
        )
    }
    if (locked) {
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
    Text(
        text = "$score points",
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.labelSmall
    )
    Text(
        text = "$numComments comments",
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.labelSmall
    )
    TimeStamp(
        time = publishedTime,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.labelSmall
    )
}


@Composable
fun ExpandButton(
    expanded: Boolean,
    onExpand: () -> Unit
) {
    IconButton(
        onClick = { onExpand() },
        modifier = Modifier
            .height(20.dp)
            .width(20.dp)
    ) {
        AnimatedContent(targetState = expanded, label = "") { expandedState ->
            Icon(
                painterResource(id = if (expandedState) R.drawable.ic_options_hide else R.drawable.ic_options_show),
                contentDescription = "options"
            )
        }
    }
}
@Composable
fun PostActions(
    onProfileClicked: () -> Unit,
    onSubredditClicked: () -> Unit,
    onBrowserClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sizeModifier = modifier.height(20.dp).width(20.dp)

    ActionButton(
        onAction = { onProfileClicked() },
        iconID = R.drawable.ic_profile,
        description = "open profile",
        modifier = sizeModifier
    )
    ActionButton(
        onAction = { onSubredditClicked() },
        iconID = R.drawable.ic_subreddit,
        description = "open subreddit",
        modifier = sizeModifier
    )
    ActionButton(
        onAction = { onBrowserClicked() },
        iconID = R.drawable.ic_open_in_browser,
        description = "open link in browser",
        modifier = sizeModifier
    )
}

@Composable
fun ActionButton(
    onAction: () -> Unit,
    iconID: Int,
    description: String,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { onAction() },
        modifier = modifier
    ) {
        Icon(
            painterResource(id = iconID),
            contentDescription = description,
        )
    }
}