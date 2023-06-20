package com.example.lurkforreddit.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PostDetailsScreen(
    commentState: CommentState,
    modifier: Modifier = Modifier
) {
    when (commentState) {
        is CommentState.Loading -> LoadingScreen(modifier)
        is CommentState.Success -> {
            Column(modifier = modifier.verticalScroll(rememberScrollState(0))) {
                Text(text = commentState.postData.first.toString())
                Text(commentState.postData.second.toString())
            }
        }
        is CommentState.Error -> ErrorScreen(modifier)
    }
}