package com.example.lurkforreddit.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lurkforreddit.network.model.PostApi
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun PostCard(
    content: PostApi,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            if (!content.is_self) {
                val thumbnail = if (content.preview != null) {
                    content.preview.jsonObject["images"]?.jsonArray?.get(0)?.jsonObject?.get("source")?.jsonObject?.get(
                        "url"
                    )?.jsonPrimitive?.contentOrNull
                } else if ((content.thumbnail == "image") || (content.thumbnail == "default")) {
                    null
                } else {
                    content.thumbnail
                }

                if (thumbnail != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(thumbnail)
                            .crossfade(true)
                            .build(),
                        placeholder = null,
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .width(75.dp)
                            .height(75.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .fillMaxWidth()
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
                    style = MaterialTheme.typography.titleSmall
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = content.subreddit,
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.labelLarge
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
                    Text(
                        text = "${content.score} points",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,

                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "${content.numComments} comments",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium
                    )
                    //Text(text = content.created.toString(), style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}