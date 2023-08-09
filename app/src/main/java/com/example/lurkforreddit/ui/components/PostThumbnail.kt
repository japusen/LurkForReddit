package com.example.lurkforreddit.ui.components

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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


@Composable
fun PostThumbnail(
    thumbnail: String,
    url: String,
    openLink: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        PostImage(
            thumbnail = thumbnail,
            url = url,
            openLink = openLink
        )
    }
}

@Composable
fun PostImage(
    thumbnail: String,
    url: String,
    openLink: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    if (thumbnail == "none") {
        Image(
            alignment = Alignment.Center,
            painter = painterResource(R.drawable.ic_link_thumbnail),
            contentDescription = null,
            contentScale = ContentScale.None,
            modifier = modifier
                .fillMaxSize()
                .clickable {
                    val intent = CustomTabsIntent
                        .Builder()
                        .build()
                    intent.launchUrl(context, Uri.parse(url))
                },
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
        )
    } else {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(thumbnail)
                .crossfade(true)
                .build(),
            placeholder = null,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxSize()
                .clickable {
                    openLink(url)
                }
        )
    }
}