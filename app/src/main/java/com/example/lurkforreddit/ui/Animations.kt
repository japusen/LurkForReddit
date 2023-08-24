package com.example.lurkforreddit.ui

import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

val fadeIn = fadeIn(
    animationSpec = tween(durationMillis = 100)
)

val fadeOut = fadeOut(
    animationSpec = tween(durationMillis = 100)
)

val slideFromLeftEdge = slideInHorizontally(
    animationSpec = tween(
        durationMillis = 250,
        easing = EaseIn
    ),
    initialOffsetX = { width -> -width } // Start off-screen to the left

)

val slideToLeftEdge = slideOutHorizontally(
    animationSpec = tween(
        durationMillis = 250,
        easing = EaseOut
    ),
    targetOffsetX = { width -> -width } // Move off-screen to the left
)
