package com.example.lurkforreddit.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ImageLink(
    url: String,
    modifier: Modifier = Modifier
) {
    val minScale = 1.0F
    val maxScale = 5.0F
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var size by remember { mutableStateOf(IntSize.Zero) }
    val context = LocalContext.current
    Scaffold(
        containerColor = Color.Black
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(url)
                    .crossfade(true)
                    .build(),
                placeholder = null,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { size = it }
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = maxOf(minScale, minOf(scale * zoom, maxScale))
                            val maxX = (size.width * (scale - 1)) / 2
                            val minX = -maxX
                            offsetX = maxOf(minX, minOf(maxX, offsetX + pan.x))
                            val maxY = (size.height * (scale - 1)) / 2
                            val minY = -maxY
                            offsetY = maxOf(minY, minOf(maxY, offsetY + pan.y))
                        }
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (scale != minScale) {
                                    scale = minScale
                                    offsetX = 0.0F
                                    offsetY = 0.0F
                                } else {
                                    scale = maxScale / 2
                                }

                            }
                        )
                    }
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    )
            )
        }
    }
}