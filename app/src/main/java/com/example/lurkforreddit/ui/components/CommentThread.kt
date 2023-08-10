package com.example.lurkforreddit.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.dp
import com.example.lurkforreddit.model.Comment
import com.example.lurkforreddit.model.CommentThreadItem
import com.example.lurkforreddit.model.More
import com.example.lurkforreddit.model.Post

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommentThread(
    post: Post,
    thread: List<CommentThreadItem>,
    openLink: (String) -> Unit,
    openProfile: (String) -> Unit,
    onChangeVisibility: (Int, Int) -> Unit,
    onMoreClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {

        item {
            CommentsHeader(
                post = post,
                openLink = openLink
            )
        }

        items(thread.size) { index ->
            thread[index].let { item ->
                AnimatedVisibility(
                    visible = item.visible,
                    enter = slideInVertically(
                        // Start the slide from 40 (pixels) above where the content is supposed to go, to
                        // produce a parallax effect
                        initialOffsetY = { -40 }
                    ) + expandVertically(
                        expandFrom = Alignment.Top
                    ) + scaleIn(
                        initialScale = 0.5f,
                        // Animate scale from 0.5f to 1f using the top center as the pivot point.
                        transformOrigin = TransformOrigin(0.5f, 0f)
                    ) + fadeIn(initialAlpha = 0.3f),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut() + scaleOut(targetScale = 1.2f)
                ) {
                    val longClickModifier = Modifier.combinedClickable(
                            enabled = true,
                            onLongClick = { onChangeVisibility(index, item.depth) },
                            onClick = {  }
                        )
                    when(item) {
                        is Comment ->
                            CommentCard(
                                postAuthor = post.author,
                                commentAuthor = item.author,
                                score = item.score,
                                createdUtc = item.createdUtc,
                                body = item.body,
                                numReplies = 0,
                                showReplies = true,
                                scoreHidden = item.scoreHidden,
                                depth = item.depth,
                                openProfile = openProfile,
                                modifier = longClickModifier
                            )
                        is More ->
                            if (item.children.isNotEmpty())
                                MoreCommentsIndicator(
                                    depth = item.depth,
                                    numberOfComments = item.children.size,
                                    onMoreClicked = { onMoreClicked(index) }
                                )
                    }
                }
            }
        }
    }
}