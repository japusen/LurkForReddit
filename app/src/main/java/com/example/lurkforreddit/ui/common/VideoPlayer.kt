package com.example.lurkforreddit.ui.common

import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun VideoPlayer(
    uri: Uri,
    onBackClicked: () -> Unit,
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                // Build the media item.
                val mediaItem = MediaItem.fromUri(uri)
                // Set the media item to be played.
                setMediaItem(mediaItem)
                // Prepare the player.
                prepare()
            }
    }

    BackHandler {
        exoPlayer.release()
        onBackClicked()
    }

    exoPlayer.playWhenReady = true

    Scaffold(
        containerColor = Color.Black
    ) { paddingValues ->
        AndroidView(
            modifier = Modifier
                .background(Color.Black)
                .padding(paddingValues)
            ,
            factory = {
                PlayerView(context).apply {
                    useController = true
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    setShowFastForwardButton(false)
                    setShowRewindButton(false)
                    setShowNextButton(false)
                    setShowPreviousButton(false)
                    player = exoPlayer
                    layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                }
            }
        )
    }
}
