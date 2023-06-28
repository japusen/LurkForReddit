package com.example.lurkforreddit.ui.components

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lurkforreddit.R
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun PostThumbnail(
    preview: JsonElement?,
    thumbnail: String,
    nsfw: Boolean,
    url: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    if (preview != null) {
        val img = if (nsfw) {
            preview.jsonObject["images"]?.jsonArray?.get(0)?.jsonObject?.get("variants")?.jsonObject?.get("nsfw")?.jsonObject?.get("source")?.jsonObject?.get("url")?.jsonPrimitive?.contentOrNull
        } else {
            preview.jsonObject["images"]?.jsonArray?.get(0)?.jsonObject?.get("source")?.jsonObject?.get("url")?.jsonPrimitive?.contentOrNull
        }
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(img)
                .crossfade(true)
                .build(),
            placeholder = null,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    val intent = CustomTabsIntent
                        .Builder()
                        .build()
                    intent.launchUrl(context, Uri.parse(url))
                }
        )
    } else if ((thumbnail != "image") && (thumbnail != "default")) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(thumbnail)
                .crossfade(true)
                .build(),
            placeholder = null,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    val intent = CustomTabsIntent
                        .Builder()
                        .build()
                    intent.launchUrl(context, Uri.parse(url))
                }
        )
    } else {
        Image(
            alignment = Alignment.Center,
            painter = painterResource(R.drawable.ic_link_thumbnail),
            contentDescription = null,
            contentScale = ContentScale.None,
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    val intent = CustomTabsIntent
                        .Builder()
                        .build()
                    intent.launchUrl(context, Uri.parse(url))
                },
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
        )
    }
}