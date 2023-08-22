package com.example.lurkforreddit.ui.components.comment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
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
import com.example.lurkforreddit.data.remote.model.PostDto
import com.example.lurkforreddit.ui.components.post.PostThumbnail
import com.example.lurkforreddit.ui.components.TimeStamp
import com.example.lurkforreddit.util.relativeTime
import kotlinx.datetime.DateTimePeriod

@Composable
fun CommentsHeader(
    postDto: PostDto,
    openLink: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 16.dp)
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            if (!postDto.isSelfPost) {
                PostThumbnail(
                    thumbnail = postDto.thumbnail,
                    url = postDto.url,
                    openLink = openLink,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                
                PostSummary(
                    title = postDto.title,
                    author = postDto.author,
                    nsfw = postDto.over18,
                    locked = postDto.locked,
                    score = postDto.score,
                    numComments = postDto.numComments,
                    publishedTime = relativeTime(postDto.createdUtc)
                )

                if (postDto.selftext != "") {
                    SelfText(text = postDto.selftext)
                }
            }
        }
    }
}

@Composable
fun PostSummary(
    title: String,
    author: String,
    nsfw: Boolean,
    locked: Boolean,
    score: Int,
    numComments: Int,
    publishedTime: DateTimePeriod
) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleMedium
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = author,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleSmall
        )

        if (nsfw) {
            Text(
                text = "nsfw",
                color = Color.Red,
                style = MaterialTheme.typography.titleSmall
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
                    .width(18.dp)
                    .height(18.dp)
            )
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "$score points",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = "$numComments comments",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleSmall
        )
        TimeStamp(
            time = publishedTime,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun SelfText(
    text: String,
    modifier: Modifier = Modifier
) {
    Divider(modifier = Modifier.padding(top = 4.dp))
    Text(
        text = text,
        modifier = modifier.padding(top = 4.dp, bottom = 8.dp)
    )
}