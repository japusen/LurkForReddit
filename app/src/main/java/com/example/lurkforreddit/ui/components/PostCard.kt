package com.example.lurkforreddit.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lurkforreddit.R
import com.example.lurkforreddit.network.model.PostApi
import com.example.lurkforreddit.util.relativeTime


@Composable
fun PostCard(
    content: PostApi,
    onPostClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onPostClicked() }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            if (!content.is_self) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
                        .width(75.dp)
                        .height(75.dp)
                        .clip(RoundedCornerShape(6.dp))
                ) {
                    PostThumbnail(
                        preview = content.preview,
                        thumbnail = content.thumbnail,
                        nsfw = content.over18,
                        url = content.url
                    )
                }
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
                ) {
                    Text(
                        text = content.subreddit,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = content.author,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (content.over18) {
                        Text(
                            text = "nsfw",
                            color = Color.Red,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    if (content.locked) {
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
                        text = "${content.score} points",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = "${content.numComments} comments",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall
                    )
                    TimeStamp(
                        time = relativeTime(content.createdUtc),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}